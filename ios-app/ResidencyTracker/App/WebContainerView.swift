import SwiftUI
import WebKit

struct WebContainerView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        let userContentController = WKUserContentController()

        // Register Native JavaScript Bridge
        userContentController.add(context.coordinator, name: "iosBridge")
        configuration.userContentController = userContentController

        // Enable local file execution and storage
        configuration.defaultWebpagePreferences.allowsContentJavaScript = true
        configuration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")

        let webView = WKWebView(frame: .zero, configuration: configuration)
        webView.navigationDelegate = context.coordinator
        webView.uiDelegate = context.coordinator
        webView.isOpaque = false
        webView.backgroundColor = .systemBackground
        webView.scrollView.bounces = true
        webView.scrollView.keyboardDismissMode = .interactive
        webView.scrollView.contentInsetAdjustmentBehavior = .never

        context.coordinator.webView = webView

        // Load local offline Web bundle
        if let url = Bundle.main.url(forResource: "index", withExtension: "html", subdirectory: "Web") {
            webView.loadFileURL(url, allowingReadAccessTo: url.deletingLastPathComponent())
        } else if let url = Bundle.main.url(forResource: "index", withExtension: "html") {
            webView.loadFileURL(url, allowingReadAccessTo: url.deletingLastPathComponent())
        } else {
            print("Error: Could not locate index.html in app bundle.")
        }

        return webView
    }

    func updateUIView(_ uiView: WKWebView, context: Context) {}

    // MARK: - Coordinator
    class Coordinator: NSObject, WKNavigationDelegate, WKUIDelegate, WKScriptMessageHandler {
        var parent: WebContainerView
        weak var webView: WKWebView?

        init(_ parent: WebContainerView) {
            self.parent = parent
        }

        // MARK: - WKScriptMessageHandler
        func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
            guard message.name == "iosBridge",
                  let body = message.body as? [String: Any],
                  let action = body["action"] as? String else {
                return
            }

            DispatchQueue.main.async { [weak self] in
                guard let self = self else { return }
                switch action {
                case "exportFile":
                    self.handleFileExport(body)
                case "printReport":
                    self.handlePrintReport()
                default:
                    print("Unknown action received from JavaScript: \(action)")
                }
            }
        }

        // MARK: - Export File Handler (UIActivityViewController)
        private func handleFileExport(_ body: [String: Any]) {
            guard let filename = body["filename"] as? String,
                  let content = body["content"] as? String else {
                return
            }

            let tempDir = FileManager.default.temporaryDirectory
            let fileURL = tempDir.appendingPathComponent(filename)

            do {
                try content.write(to: fileURL, atomically: true, encoding: .utf8)
                presentActivityController(items: [fileURL])
            } catch {
                print("Failed to save temporary export file: \(error.localizedDescription)")
            }
        }

        private func presentActivityController(items: [Any]) {
            guard let topVC = getTopViewController() else { return }
            let activityVC = UIActivityViewController(activityItems: items, applicationActivities: nil)

            // iPad Popover Presentation Support
            if let popover = activityVC.popoverPresentationController {
                popover.sourceView = topVC.view
                popover.sourceRect = CGRect(x: topVC.view.bounds.midX, y: topVC.view.bounds.midY, width: 0, height: 0)
                popover.permittedArrowDirections = []
            }

            topVC.present(activityVC, animated: true)
        }

        // MARK: - Print Report Handler (UIPrintInteractionController)
        private func handlePrintReport() {
            guard let webView = self.webView else { return }

            let printController = UIPrintInteractionController.shared
            let printInfo = UIPrintInfo(dictionary: nil)
            printInfo.outputType = .general
            printInfo.jobName = "تقرير احتساب مدة الإقامة"
            printInfo.orientation = .portrait

            printController.printInfo = printInfo
            printController.showsPageRange = true
            printController.printFormatter = webView.viewPrintFormatter()

            guard let topVC = getTopViewController() else { return }

            if UIDevice.current.userInterfaceIdiom == .pad {
                printController.present(from: topVC.view.bounds, in: topVC.view, animated: true, completionHandler: nil)
            } else {
                printController.present(animated: true, completionHandler: nil)
            }
        }

        // MARK: - WKUIDelegate (Native Arabic Dialogs)
        func webView(_ webView: WKWebView, runJavaScriptAlertPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping () -> Void) {
            guard let topVC = getTopViewController() else {
                completionHandler()
                return
            }

            let alert = UIAlertController(title: "تنبيه", message: message, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "حسناً", style: .default) { _ in
                completionHandler()
            })
            topVC.present(alert, animated: true)
        }

        func webView(_ webView: WKWebView, runJavaScriptConfirmPanelWithMessage message: String, initiatedByFrame frame: WKFrameInfo, completionHandler: @escaping (Bool) -> Void) {
            guard let topVC = getTopViewController() else {
                completionHandler(false)
                return
            }

            let alert = UIAlertController(title: "تأكيد", message: message, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "نعم", style: .destructive) { _ in
                completionHandler(true)
            })
            alert.addAction(UIAlertAction(title: "إلغاء", style: .cancel) { _ in
                completionHandler(false)
            })
            topVC.present(alert, animated: true)
        }

        // MARK: - Topmost UIViewController Discovery
        private func getTopViewController() -> UIViewController? {
            guard let windowScene = UIApplication.shared.connectedScenes
                    .compactMap({ $0 as? UIWindowScene })
                    .first(where: { $0.activationState == .foregroundActive || $0.activationState == .foregroundInactive }),
                  let rootVC = windowScene.windows.first(where: { $0.isKeyWindow })?.rootViewController else {
                return nil
            }

            var topController = rootVC
            while let presented = topController.presentedViewController {
                topController = presented
            }
            return topController
        }
    }
}
