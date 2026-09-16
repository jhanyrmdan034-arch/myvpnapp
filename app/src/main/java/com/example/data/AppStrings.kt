package com.example.data

object AppStrings {
    fun get(key: String, lang: String): String {
        val isFa = lang == "fa"
        val isAr = lang == "ar"
        val isEs = lang == "es"
        val isTr = lang == "tr"
        val isDe = lang == "de"
        val isFr = lang == "fr"

        return when (key) {
            "app_name" -> "VectaVPN"
            "select_language_title" -> when {
                isFa -> "انتخاب زبان"
                isAr -> "اختر اللغة"
                isEs -> "Seleccionar Idioma"
                isTr -> "Dil Seçin"
                isDe -> "Sprache wählen"
                isFr -> "Choisir la langue"
                else -> "Select Language"
            }
            "select_language_subtitle" -> when {
                isFa -> "لطفاً زبان مورد نظر خود را برای ادامه انتخاب کنید"
                isAr -> "يرجى تحديد لغتك المفضلة للمتابعة"
                isEs -> "Por favor seleccione su idioma preferido para continuar"
                isTr -> "Devam etmek için lütfen tercih ettiğiniz dili seçin"
                isDe -> "Bitte wählen Sie Ihre bevorzugte Sprache aus"
                isFr -> "Veuillez choisir votre langue préférée pour continuer"
                else -> "Please choose your preferred language to continue"
            }
            "continue_btn" -> when {
                isFa -> "ادامه"
                isAr -> "متابعة"
                isEs -> "Continuar"
                isTr -> "Devam Et"
                isDe -> "Weiter"
                isFr -> "Continuer"
                else -> "Continue"
            }
            "privacy_title" -> when {
                isFa -> "سیاست حفظ حریم خصوصی و شرایط"
                isAr -> "سياسة الخصوصية والشروط"
                isEs -> "Política de Privacidad y Términos"
                isTr -> "Gizlilik Politikası ve Şartlar"
                isDe -> "Datenschutz & Nutzungsbedingungen"
                isFr -> "Politique de confidentialité et conditions"
                else -> "Privacy Policy & Terms"
            }
            "privacy_subtitle" -> when {
                isFa -> "لطفاً شرایط خدمات و سیاست حفظ حریم خصوصی ما را مطالعه نمایید"
                isAr -> "يرجى قراءة شروط الخدمة وسياسة الخصوصية الخاصة بنا"
                isEs -> "Por favor revise nuestros términos de servicio y privacidad"
                isTr -> "Lütfen hizmet şartlarımızı ve gizlilik politikamızı inceleyin"
                isDe -> "Bitte überprüfen Sie unsere Nutzungsbedingungen und Datenschutzrichtlinien"
                isFr -> "Veuillez lire nos conditions d'utilisation et politique de confidentialité"
                else -> "Please review our terms of service and privacy practices"
            }
            "privacy_agreement" -> when {
                isFa -> "من با سیاست حفظ حریم خصوصی و شرایط خدمات موافقم."
                isAr -> "أوافق على سياسة الخصوصية وشروط الخدمة."
                isEs -> "Acepto la Política de Privacidad y los Términos de Servicio."
                isTr -> "Gizlilik Politikasını ve Hizmet Şartlarını kabul ediyorum."
                isDe -> "Ich stimme der Datenschutzrichtlinie und den AGB zu."
                isFr -> "J'accepte la politique de confidentialité et les conditions d'utilisation."
                else -> "I agree to the Privacy Policy and Terms of Service."
            }
            "accept_and_continue" -> when {
                isFa -> "پذیرش و ادامه"
                isAr -> "قبول ومتابعة"
                isEs -> "Aceptar y Continuar"
                isTr -> "Kabul Et ve Devam Et"
                isDe -> "Akzeptieren & Weiter"
                isFr -> "Accepter et continuer"
                else -> "Accept & Continue"
            }
            "tap_to_connect" -> when {
                isFa -> "برای اتصال لمس کنید"
                isAr -> "اضغط للاتصال"
                isEs -> "Toca para conectar"
                isTr -> "Bağlanmak için dokunun"
                isDe -> "Tippen zum Verbinden"
                isFr -> "Toucher pour connecter"
                else -> "Tap to Connect"
            }
            "connecting" -> when {
                isFa -> "در حال اتصال..."
                isAr -> "جارٍ الاتصال..."
                isEs -> "Conectando..."
                isTr -> "Bağlanıyor..."
                isDe -> "Verbinde..."
                isFr -> "Connexion en cours..."
                else -> "Connecting..."
            }
            "connected" -> when {
                isFa -> "متصل شد"
                isAr -> "متصل"
                isEs -> "Conectado"
                isTr -> "Bağlandı"
                isDe -> "Verbunden"
                isFr -> "Connecté"
                else -> "Connected"
            }
            "vpn_off" -> when {
                isFa -> "خاموش"
                isAr -> "إيقاف"
                isEs -> "Apagado"
                isTr -> "Kapalı"
                isDe -> "Aus"
                isFr -> "Éteint"
                else -> "OFF"
            }
            "vpn_on" -> when {
                isFa -> "روشن"
                isAr -> "تشغيل"
                isEs -> "Encendido"
                isTr -> "Açık"
                isDe -> "Ein"
                isFr -> "Allumé"
                else -> "ON"
            }
            "vpn_disconnected_badge" -> when {
                isFa -> "قطع"
                isAr -> "غير متصل"
                isEs -> "Desconectado"
                isTr -> "Bağlantı Yok"
                isDe -> "Getrennt"
                isFr -> "Déconnecté"
                else -> "Disconnected"
            }
            "vpn_connected_badge" -> when {
                isFa -> "متصل"
                isAr -> "متصل"
                isEs -> "Conectado"
                isTr -> "Bağlandı"
                isDe -> "Verbunden"
                isFr -> "Connecté"
                else -> "Connected"
            }
            "close_card" -> when {
                isFa -> "بستن کادر"
                isAr -> "إغلاق"
                isEs -> "Cerrar"
                isTr -> "Kapat"
                isDe -> "Schließen"
                isFr -> "Fermer"
                else -> "Collapse"
            }
            "disconnecting" -> when {
                isFa -> "در حال قطع اتصال..."
                isAr -> "جارٍ قطع الاتصال..."
                isEs -> "Desconectando..."
                isTr -> "Bağlantı kesiliyor..."
                isDe -> "Trennen..."
                isFr -> "Déconnexion..."
                else -> "Disconnecting..."
            }
            "select_location" -> when {
                isFa -> "انتخاب مکان"
                isAr -> "تحديد الموقع"
                isEs -> "Seleccionar Ubicación"
                isTr -> "Konum Seçin"
                isDe -> "Standort wählen"
                isFr -> "Sélectionner un serveur"
                else -> "Select Location"
            }
            "tab_home" -> when {
                isFa -> "خانه"
                isAr -> "الرئيسية"
                isEs -> "Inicio"
                isTr -> "Ana Sayfa"
                isDe -> "Start"
                isFr -> "Accueil"
                else -> "Home"
            }
            "tab_security" -> when {
                isFa -> "امنیت"
                isAr -> "الأمان"
                isEs -> "Seguridad"
                isTr -> "Güvenlik"
                isDe -> "Sicherheit"
                isFr -> "Sécurité"
                else -> "Security"
            }
            "tab_profile" -> when {
                isFa -> "پروفایل"
                isAr -> "الملف الشخصي"
                isEs -> "Perfil"
                isTr -> "Profil"
                isDe -> "Profil"
                isFr -> "Profil"
                else -> "Profile"
            }
            "kill_switch" -> when {
                isFa -> "سوئیچ قطع اضطراری (Kill Switch)"
                isAr -> "مفتاح القفل (Kill Switch)"
                else -> "Kill Switch"
            }
            "kill_switch_desc" -> when {
                isFa -> "قطع خودکار ترافیک اینترنت در صورت قطع ناگهانی VPN"
                else -> "Block all traffic if VPN connection drops unexpectedly"
            }
            "dns_leak_protect" -> when {
                isFa -> "محافظت از نشت DNS"
                else -> "DNS Leak Protection"
            }
            "dns_leak_desc" -> when {
                isFa -> "هدایت تمام درخواست‌های DNS از طریق تونل رمزگذاری‌شده"
                else -> "Route all DNS requests through encrypted private resolver"
            }
            "vpn_protocol" -> when {
                isFa -> "پروتکل VPN"
                else -> "VPN Protocol"
            }
            "virtual_ip" -> when {
                isFa -> "آدرس آی‌پی مجازی"
                else -> "Virtual IP"
            }
            "download_speed" -> when {
                isFa -> "دانلود"
                else -> "Download"
            }
            "upload_speed" -> when {
                isFa -> "آپلود"
                else -> "Upload"
            }
            "duration" -> when {
                isFa -> "مدت اتصال"
                else -> "Duration"
            }
            "change_language" -> when {
                isFa -> "تغییر زبان برنامه"
                else -> "Change App Language"
            }
            "restart_onboarding" -> when {
                isFa -> "اجرای مجدد راهنمای اولیه"
                else -> "Replay Onboarding Flow"
            }
            "settings" -> when {
                isFa -> "تنظیمات"
                else -> "Settings"
            }
            else -> key
        }
    }

    val samplePrivacyPolicyPersian = """
        سیاست حفظ حریم خصوصی و شرایط خدمات VectaVPN
        
        ۱. خط‌مشی عدم ثبت وقایع (Strict No-Logs Policy):
        ما در VectaVPN به حریم خصوصی شما احترام کامل می‌گذاریم. سرورهای ما هیچ‌گونه لاگ یا داده‌ای از ترافیک وب، مقصد ارتباطی، داده‌های تبادل‌شده یا کوئری‌های DNS شما را جمع‌آوری، ذخیره یا به اشتراک نمی‌گذارند.
        
        ۲. رمزنگاری سرتاسری ترافیک:
        تمام داده‌های خروجی و ورودی دستگاه شما از طریق الگوریتم‌های رمزنگاری پیشرفته نظامی AES-256-GCM و پروتکل‌های مدرن ChaCha20/Poly1305 محافظت می‌شوند تا ارتباط شما در شبکه‌های عمومی و وای‌فای کاملاً ایمن باشد.
        
        ۳. حفاظت از هویت و آی‌پی:
        آدرس IP واقعی شما با آدرس سرور منتخب ماسک شده و از نشت اطلاعات مکانی و هویتی جلوگیری به عمل می‌آید.
        
        ۴. شرایط استفاده:
        استفاده از سرویس برای فعالیت‌های غیرقانونی، تخریب شبکه‌ها یا نقض حقوق دیگران ممنوع است. سرویس صرفاً جهت حفظ حریم خصوصی، ارتقای امنیت و اتصال آزاد به وب ارائه می‌شود.
        
        با زدن دکمه «پذیرش و ادامه»، شما موافقت خود را با تمامی بندهای فوق اعلام می‌دارید.
    """.trimIndent()

    val samplePrivacyPolicyEnglish = """
        VectaVPN - Privacy Policy & Terms of Service
        
        1. Strict No-Logs Commitment:
        VectaVPN operates under a strict zero-log policy. We do not inspect, log, store, or share your browsing history, connection timestamps, traffic destinations, DNS queries, or IP logs.
        
        2. Military-Grade Tunnel Encryption:
        All inbound and outbound communications are encrypted using high-performance cryptographic ciphers (AES-256-GCM and ChaCha20) to defend your identity against eavesdropping on public Wi-Fi and ISP monitoring.
        
        3. Virtual IP Masking & DNS Shield:
        Your authentic public IP address is dynamically replaced with the high-speed gateway of the selected location. Built-in leak shields prevent IPv6 and WebRTC leaks.
        
        4. Fair Use Terms:
        You agree to utilize VectaVPN solely for personal security, privacy protection, and lawful network transmission.
        
        By tapping "Accept & Continue", you confirm that you have read, understood, and agree to these terms.
    """.trimIndent()
}
