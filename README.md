# ResidencyTracker iOS — متتبع أيام الإقامة الدراسية

تطبيق iOS مجاني ومفتوح المصدر مخصص لطلبة الدراسات العليا (الماجستير والدكتوراه) لحساب وتتبع أيام الإقامة الفعلية في الخارج بدقة وفق متطلبات معادلة الشهادات.

## ✨ المميزات
- **يعمل بدون إنترنت 100% (Offline-First):** جميع السجلات والإعدادات تُحفظ محلياً على جهازك فقط.
- **حساب دقيق لأيام الإقامة:** تسجيل تواريخ الخروج والدخول وأرقام صفحات الجواز والملاحظات، مع احتساب الأيام المتبقية لتحقيق الهدف المطلوب (مثلاً 180 يوماً).
- **واجهة عربية متكاملة (`RTL`):** تدعم الوضع الفاتح والداكن مع منتقي تواريخ عربي مدمج (`Flatpickr`).
- **تطبيق iOS أصلي خفيف:** مبني بـ SwiftUI + WKWebView مع جسر محلي للحفظ الآمن.

## 📲 التثبيت على الآيفون (iOS)
1. انتقل إلى صفحة **[Releases](../../releases)** وحمّل ملف **`ResidencyTracker-Unsigned.ipa`**.
2. ثبّت التطبيق على جهازك باستخدام أحد برامج التوقيع الذاتي المجانية مثل:
   - **AltStore** / **SideStore**
   - **Sideloadly**

## 🛠️ البناء من المصدر
يتم بناء نسخة `.ipa` غير الموقّعة تلقائياً عبر GitHub Actions (`macos-latest` + `XcodeGen`). لبنائها محلياً على macOS:
```bash
cd ios-app
xcodegen generate
xcodebuild archive -project ResidencyTracker.xcodeproj -scheme ResidencyTracker -configuration Release -destination "generic/platform=iOS" -archivePath build/ResidencyTracker.xcarchive CODE_SIGNING_ALLOWED=NO CODE_SIGNING_IDENTITY="" CODE_SIGNING_REQUIRED=NO
```

## 👨‍💻 المطور
- **Hussein Ali** — [GitHub @huss200](https://github.com/huss200) | [Instagram @pt2b](https://www.instagram.com/pt2b)

## 📄 الرخصة
مرخص تحت رخصة [MIT](LICENSE).
