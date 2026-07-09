# 🛍️ ঊষা - একক বিক্রেতার ই-কমার্স অ্যাপ

**ঊষা** একটি Android মোবাইল অ্যাপ্লিকেশন যেখানে শুধুমাত্র একজন বিক্রেতা (আপনি) পণ্য বিক্রয় করতে পারেন এবং ক্রেতারা পণ্য কিনতে পারেন।

## 🎯 বৈশিষ্ট্য

✅ **পণ্য ব্যবস্থাপনা**
- পণ্য যোগ করুন, সম্পাদনা করুন এবং মুছুন
- প্রতিটি পণ্যের জন্য ছবি, বর্ণনা এবং মূল্য

✅ **ক্রেতা ক্রয় সিস্টেম**
- পণ্য ব্রাউজ করুন এবং অনুসন্ধান করুন
- শপিং কার্টে পণ্য যোগ করুন
- অর্ডার প্লেস করুন

✅ **পেমেন্ট ইন্টিগ্রেশন**
- bKash পেমেন্ট গেটওয়ে সংযোগ

✅ **অর্ডার ব্যবস্থাপনা**
- অর্ডার ট্র্যাকিং
- ডেলিভারি স্ট্যাটাস আপডেট

## 📱 প্রযুক্তি স্ট্যাক

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Database:** Firebase Firestore
- **Authentication:** Firebase Auth
- **Payment:** bKash API
- **Backend:** Firebase Functions

## 📂 প্রজেক্ট স্ট্রাকচার

```
Usha-/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/usha/
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   ├── ProductDetailScreen.kt
│   │   │   │   │   │   ├── CartScreen.kt
│   │   │   │   │   │   └── CheckoutScreen.kt
│   │   │   │   │   └── components/
│   │   │   │   ├── viewmodel/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   ├── repository/
│   │   │   │   │   └── local/
│   │   │   │   ├── network/
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## 🚀 শুরু করুন

### প্রয়োজনীয়তা
- Android Studio (সর্বশেষ সংস্করণ)
- JDK 11+
- Android SDK 21+

### সেটআপ

1. **রিপো ক্লোন করুন**
```bash
git clone https://github.com/abdulmominmir07-commits/Usha-.git
cd Usha-
```

2. **Android Studio এ খুলুন**
   - Android Studio এ প্রজেক্ট খুলুন

3. **Dependencies ইনস্টল করুন**
```bash
./gradlew build
```

4. **Run করুন**
   - একটি Android ডিভাইস বা এমুলেটর সংযুক্ত করুন
   - `./gradlew installDebug` চালান

## 🔐 Firebase সেটআপ

1. [Firebase Console](https://console.firebase.google.com) এ যান
2. নতুন প্রজেক্ট তৈরি করুন
3. Android অ্যাপ যোগ করুন
4. `google-services.json` ফাইল ডাউনলোড করুন
5. `app/` ফোল্ডারে রাখুন

## 💳 bKash ইন্টিগ্রেশন

bKash API দিয়ে সংযোগের জন্য:
1. bKash ডেভেলপার পোর্টালে রেজিস্ট্রেশন করুন
2. API সাক্ষাৎপত্র পান
3. `local.properties` এ যোগ করুন:
```
bkash.api_key=YOUR_API_KEY
bkash.secret_key=YOUR_SECRET_KEY
```

## 📝 মূল কম্পোনেন্ট

### Models
- `Product.kt` - পণ্য ডেটা মডেল
- `Order.kt` - অর্ডার ডেটা মডেল
- `User.kt` - ব্যবহারকারী ডেটা মডেল
- `CartItem.kt` - কার্ট আইটেম মডেল

### ViewModels
- `HomeViewModel.kt` - হোম স্ক্রিন লজিক
- `ProductViewModel.kt` - পণ্য বিস্তারিত লজিক
- `CartViewModel.kt` - কার্ট ম্যানেজমেন্ট লজিক
- `CheckoutViewModel.kt` - চেকআউট এবং পেমেন্ট লজিক

### Repositories
- `ProductRepository.kt` - পণ্য ডেটা অ্যাক্সেস
- `OrderRepository.kt` - অর্ডার ডেটা অ্যাক্সেস
- `PaymentRepository.kt` - পেমেন্ট প্রসেসিং

## 🧪 পরীক্ষা

ইউনিট টেস্ট চালান:
```bash
./gradlew test
```

## 📄 লাইসেন্স

এই প্রজেক্ট MIT লাইসেন্সের অধীন।

## 📞 সহায়তা

যে কোনো প্রশ্ন বা সমস্যার জন্য GitHub Issues খুলুন।

---

**উন্নয়ন শুরু করুন এবং আপনার অনলাইন স্টোর লঞ্চ করুন!** 🚀
