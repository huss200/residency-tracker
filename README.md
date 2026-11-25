# ResidencyTracker — متتبع أيام الإقامة للدراسة بالخارج (iOS & Android)

> تطبيق مجاني ومفتوح المصدر مخصص لطلبة الدراسات العليا (الماجستير والدكتوراه) لحساب وتتبع فترات السفر وأيام الإقامة الفعلية في بلد الدراسة بدقة وفق متطلبات وتعليمات معادلة الشهادات الأكاديمية.

---

## ✨ المميزات الرئيسية
- 🔒 **يعمل بدون إنترنت 100% (Offline-First):** لا يحتاج إلى أي خوادم خارجية ولا يطلب أي صلاحيات إنترنت (`No Internet Permission`). جميع بيانات السفر والإعدادات تُحفظ محلياً على جهازك فقط.
- 📅 **حساب فترات السفر والإقامة بدقة:** تسجيل تواريخ الخروج والدخول، أرقام صفحات أختام الجواز، وحساب عدد الأيام تلقائياً مع تتبع الأيام المتبقية أو الفائضة عن الحد الأدنى المطلوب (مثلاً 180 يوماً).
- 🖨️ **تصدير وطباعة التقارير:** تصدير السجلات بصيغ جاهزة (CSV / JSON) أو طباعتها مباشرة كتقرير رسمي من داخل التطبيق عبر خدمات الطباعة الأصلية للنظام.
- 🎨 **واجهة عربية متكاملة (`RTL`):** تصميم حديث يدعم الوضع الفاتح والداكن تلقائياً مع منتقي تواريخ عربي مدمج ومفصل.

---

## 🏗️ المعمارية التقنية (Cross-Platform)

```
                       ┌─────────────────────────────────────────┐
                       │     Shared Web UI (HTML5 / RTL CSS)     │
                       │    Offline Engine & Arabic Datepicker   │
                       └───────────────────┬─────────────────────┘
                                           │
                     ┌─────────────────────┴─────────────────────┐
                     ▼                                           ▼
       ┌───────────────────────────┐               ┌───────────────────────────┐
       │      Android (Kotlin)     │               │        iOS (SwiftUI)      │
       ├───────────────────────────┤               ├───────────────────────────┤
       │ • Room SQLite Local DB    │               │ • WKWebView Local Bridge  │
       │ • WebViewAssetLoader      │               │ • UIActivityViewController│
       │ • Android PrintManager    │               │ • UIPrintInteraction      │
       │ • FileProvider Share      │               │ • Offline App Bundle      │
       └───────────────────────────┘               └───────────────────────────┘
```

1. **نسخة Android (`android-app/`):**
   - غلاف أصلي بلغة **Kotlin** مع قاعدة بيانات محلية مدمجة عبر **Room (SQLite)** (`residency_tracker.db`).
   - اتصال آمن بين الواجهة والنظام عبر `@JavascriptInterface` بدون أي وصول خارجي (`allowFileAccess = false`).
   - مشاركة الملفات عبر `FileProvider` والطباعة عبر `PrintManager`.

2. **نسخة iOS (`ios-app/`):**
   - تطبيق أصلي بـ **SwiftUI + WKWebView**.
   - جسر محلي عبر `WKScriptMessageHandler` لدعم التصدير عبر `UIActivityViewController` والطباعة المباشرة.

---

## 📲 التثبيت والتحميل المباشر

الملفات التنفيذية الجاهزة للتثبيت متوفرة بشكل دائم ومباشر في صفحة **[GitHub Releases](../../releases)**:

### 🤖 نظام أندرويد (Android)
1. حمّل ملف **[`ResidencyTracker-Android.apk`](../../releases/latest/download/ResidencyTracker-Android.apk)** مباشرة.
2. افتح الملف وثبّته على هاتفك فوراً (لا يحتاج لأي متجر أو إعدادات معقدة).

### 🍏 نظام آيفون (iOS)
1. حمّل ملف **[`ResidencyTracker-Unsigned.ipa`](../../releases/latest/download/ResidencyTracker-Unsigned.ipa)**.
2. ثبّت التطبيق عبر أحد برامج التوقيع الشخصي المجانية:
   - **AltStore** أو **SideStore** (مباشرة عبر الوايفاي).
   - **Sideloadly** (عبر الكمبيوتر).
   - أو عبر **TrollStore** للأجهزة المدعومة.

---

## 🛠️ البناء من المصدر (Building from Source)

يتم بناء التطبيق للنظامين تلقائياً عبر **GitHub Actions** في سحابة GitHub، ويمكنك أيضاً بناؤه محلياً:

### بناء تطبيق Android:
```bash
cd android-app
./gradlew testDebugUnitTest assembleRelease
```
يُنتج ملف `app-release.apk` داخل `android-app/app/build/outputs/apk/release/`.

### بناء تطبيق iOS (يتطلب macOS + Xcode):
```bash
cd ios-app
xcodegen generate
xcodebuild archive \
  -project ResidencyTracker.xcodeproj \
  -scheme ResidencyTracker \
  -configuration Release \
  -destination "generic/platform=iOS" \
  -archivePath build/ResidencyTracker.xcarchive \
  CODE_SIGNING_ALLOWED=NO CODE_SIGN_IDENTITY="" CODE_SIGNING_REQUIRED=NO
```

---

## 👨‍💻 المطور
- **Hussein Ali**
  - **GitHub:** [@huss200](https://github.com/huss200)
  - **Instagram:** [@pt2b](https://www.instagram.com/pt2b)

## 📄 الرخصة
المشروع مرخص بالكامل تحت رخصة **[MIT License](LICENSE)** مفتوحة المصدر للاستخدام الشخصي والتطوير الحر.
