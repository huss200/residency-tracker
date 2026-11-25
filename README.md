# ResidencyTracker iOS — متتبع أيام الإقامة الدراسية

تطبيق iOS مجاني ومفتوح المصدر مخصص لطلبة الدراسات العليا (الماجستير والدكتوراه) لحساب وتتبع أيام الإقامة الفعلية في الخارج بدقة وفق متطلبات معادلة الشهادات.

## ✨ المميزات
- **يعمل بدون إنترنت 100% (Offline-First):** جميع السجلات والإعدادات تُحفظ محلياً على جهازك فقط.
- **حساب دقيق لأيام الإقامة:** تسجيل تواريخ الخروج والدخول وأرقام صفحات الجواز والملاحظات، مع احتساب الأيام المتبقية لتحقيق الهدف المطلوب (مثلاً 180 يوماً).
- **واجهة عربية متكاملة (`RTL`):** تدعم الوضع الفاتح والداكن مع منتقي تواريخ عربي مدمج (`Flatpickr`).
- **تطبيق iOS أصلي خفيف:** مبني بـ SwiftUI + WKWebView مع جسر محلي للحفظ الآمن.

## 📲 التثبيت (iOS & Android)
- **Android (APK):** حمّل ملف **`ResidencyTracker-Android.apk`** من صفحة **[Releases](../../releases)** وثبّته مباشرة (يعمل بقاعدة بيانات SQLite / Room محلية بالكامل).
- **iOS (IPA):** حمّل ملف **`ResidencyTracker-Unsigned.ipa`** من صفحة **[Releases](../../releases)** وثبّته عبر **AltStore** / **SideStore** / **Sideloadly**.

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
