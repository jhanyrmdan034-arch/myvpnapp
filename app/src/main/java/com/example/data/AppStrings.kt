package com.example.data

object AppStrings {
    fun get(key: String, lang: String): String {
        val code = lang.lowercase().trim()
        return when (key) {
            "app_name" -> "VectaVPN"

            // 1. Language Selection
            "select_language_title" -> when (code) {
                "fa" -> "انتخاب زبان"
                "ar" -> "اختر اللغة"
                "es" -> "Seleccionar Idioma"
                "tr" -> "Dil Seçin"
                "de" -> "Sprache wählen"
                "fr" -> "Choisir la langue"
                "it" -> "Seleziona Lingua"
                "ru" -> "Выберите язык"
                "ja" -> "言語を選択"
                else -> "Select Language"
            }
            "select_language_subtitle" -> when (code) {
                "fa" -> "لطفاً زبان مورد نظر خود را برای ادامه انتخاب کنید"
                "ar" -> "يرجى تحديد لغتك المفضلة للمتابعة"
                "es" -> "Por favor seleccione su idioma preferido para continuar"
                "tr" -> "Devam etmek için lütfen tercih ettiğiniz dili seçin"
                "de" -> "Bitte wählen Sie Ihre bevorzugte Sprache aus"
                "fr" -> "Veuillez choisir votre langue préférée pour continuer"
                "it" -> "Seleziona la tua lingua preferita per continuare"
                "ru" -> "Пожалуйста, выберите язык для продолжения"
                "ja" -> "続行するには希望の言語を選択してください"
                else -> "Please choose your preferred language to continue"
            }
            "continue_btn" -> when (code) {
                "fa" -> "ادامه"
                "ar" -> "متابعة"
                "es" -> "Continuar"
                "tr" -> "Devam Et"
                "de" -> "Weiter"
                "fr" -> "Continuer"
                "it" -> "Continua"
                "ru" -> "Продолжить"
                "ja" -> "次へ"
                else -> "Continue"
            }

            // 2. Privacy Policy & Terms
            "privacy_title" -> when (code) {
                "fa" -> "سیاست حفظ حریم خصوصی و شرایط"
                "ar" -> "سياسة الخصوصية والشروط"
                "es" -> "Política de Privacidad y Términos"
                "tr" -> "Gizlilik Politikası ve Şartlar"
                "de" -> "Datenschutz & Nutzungsbedingungen"
                "fr" -> "Politique de confidentialité et conditions"
                "it" -> "Informativa sulla Privacy e Termini"
                "ru" -> "Политика конфиденциальности и условия"
                "ja" -> "プライバシーポリシーと利用規約"
                else -> "Privacy Policy & Terms"
            }
            "privacy_subtitle" -> when (code) {
                "fa" -> "لطفاً شرایط خدمات و سیاست حفظ حریم خصوصی ما را مطالعه نمایید"
                "ar" -> "يرجى قراءة شروط الخدمة وسياسة الخصوصية الخاصة بنا"
                "es" -> "Por favor revise nuestros términos de servicio y privacidad"
                "tr" -> "Lütfen hizmet şartlarımızı ve gizlilik politikamızı inceleyin"
                "de" -> "Bitte überprüfen Sie unsere Nutzungsbedingungen und Datenschutzrichtlinien"
                "fr" -> "Veuillez lire nos conditions d'utilisation et politique de confidentialité"
                "it" -> "Si prega di verificare i termini di servizio e l'informativa sulla privacy"
                "ru" -> "Пожалуйста, ознакомьтесь с условиями обслуживания и политикой конфиденциальности"
                "ja" -> "利用規約とプライバシーポリシーをご確認ください"
                else -> "Please review our terms of service and privacy practices"
            }
            "privacy_agreement" -> when (code) {
                "fa" -> "من با سیاست حفظ حریم خصوصی و شرایط خدمات موافقم."
                "ar" -> "أوافق على سياسة الخصوصية وشروط الخدمة."
                "es" -> "Acepto la Política de Privacidad y los Términos de Servicio."
                "tr" -> "Gizlilik Politikasını ve Hizmet Şartlarını kabul ediyorum."
                "de" -> "Ich stimme der Datenschutzrichtlinie und den AGB zu."
                "fr" -> "J'accepte la politique de confidentialité et les conditions d'utilisation."
                "it" -> "Accetto l'Informativa sulla Privacy e i Termini di Servizio."
                "ru" -> "Я принимаю Политику конфиденциальности и Условия использования."
                "ja" -> "プライバシーポリシーと利用規約に同意します。"
                else -> "I agree to the Privacy Policy and Terms of Service."
            }
            "accept_and_continue" -> when (code) {
                "fa" -> "پذیرش و ادامه"
                "ar" -> "قبول ومتابعة"
                "es" -> "Aceptar y Continuar"
                "tr" -> "Kabul Et ve Devam Et"
                "de" -> "Akzeptieren & Weiter"
                "fr" -> "Accepter et continuer"
                "it" -> "Accetta e Continua"
                "ru" -> "Принять и продолжить"
                "ja" -> "同意して続行"
                else -> "Accept & Continue"
            }

            // 3. Connection Button & States
            "tap_to_connect" -> when (code) {
                "fa" -> "برای اتصال لمس کنید"
                "ar" -> "اضغط للاتصال"
                "es" -> "Toca para conectar"
                "tr" -> "Bağlanmak için dokunun"
                "de" -> "Tippen zum Verbinden"
                "fr" -> "Toucher pour connecter"
                "it" -> "Tocca per connetterti"
                "ru" -> "Нажмите для подключения"
                "ja" -> "タップして接続"
                else -> "Tap to Connect"
            }
            "connecting" -> when (code) {
                "fa" -> "در حال اتصال..."
                "ar" -> "جارٍ الاتصال..."
                "es" -> "Conectando..."
                "tr" -> "Bağlanıyor..."
                "de" -> "Verbinde..."
                "fr" -> "Connexion en cours..."
                "it" -> "Connessione in corso..."
                "ru" -> "Подключение..."
                "ja" -> "接続中..."
                else -> "Connecting..."
            }
            "connected" -> when (code) {
                "fa" -> "متصل شد"
                "ar" -> "متصل"
                "es" -> "Conectado"
                "tr" -> "Bağlandı"
                "de" -> "Verbunden"
                "fr" -> "Connecté"
                "it" -> "Connesso"
                "ru" -> "Подключено"
                "ja" -> "接続完了"
                else -> "Connected"
            }
            "disconnected" -> when (code) {
                "fa" -> "قطع اتصال"
                "ar" -> "غير متصل"
                "es" -> "Desconectado"
                "tr" -> "Bağlantı Kesildi"
                "de" -> "Getrennt"
                "fr" -> "Déconnecté"
                "it" -> "Disconnesso"
                "ru" -> "Отключено"
                "ja" -> "未接続"
                else -> "Disconnected"
            }
            "connection_failed" -> when (code) {
                "fa" -> "خطا در اتصال - لمس برای تلاش مجدد"
                "ar" -> "فشل الاتصال - اضغط لإعادة المحاولة"
                "es" -> "Error de conexión - Toca para reintentar"
                "tr" -> "Bağlantı başarısız - Yeniden denemek için dokunun"
                "de" -> "Verbindung fehlgeschlagen - Tippen zum Wiederholen"
                "fr" -> "Échec de connexion - Toucher pour réessayer"
                "it" -> "Connessione fallita - Tocca per riprovare"
                "ru" -> "Ошибка подключения - Нажмите для повтора"
                "ja" -> "接続失敗 - タップして再試行"
                else -> "Connection Failed - Tap to Retry"
            }

            // 4. Status Labels
            "vpn_status_connected" -> when (code) {
                "fa" -> "متصل شد"
                "ar" -> "متصل"
                "es" -> "CONECTADO"
                "tr" -> "BAĞLANDI"
                "de" -> "VERBUNDEN"
                "fr" -> "CONNECTÉ"
                "it" -> "CONNESSO"
                "ru" -> "ПОДКЛЮЧЕНО"
                "ja" -> "接続完了"
                else -> "CONNECTED"
            }
            "vpn_status_connecting" -> when (code) {
                "fa" -> "در حال اتصال..."
                "ar" -> "جارٍ الاتصال..."
                "es" -> "CONECTANDO..."
                "tr" -> "BAĞLANIYOR..."
                "de" -> "VERBINDE..."
                "fr" -> "CONNEXION..."
                "it" -> "CONNESSIONE..."
                "ru" -> "ПОДКЛЮЧЕНИЕ..."
                "ja" -> "接続中..."
                else -> "CONNECTING..."
            }
            "vpn_status_disconnected" -> when (code) {
                "fa" -> "متصل نیست"
                "ar" -> "غير متصل"
                "es" -> "DESCONECTADO"
                "tr" -> "BAĞLANTI YOK"
                "de" -> "GETRENNT"
                "fr" -> "DÉCONNECTÉ"
                "it" -> "DISCONNESSO"
                "ru" -> "ОТКЛЮЧЕНО"
                "ja" -> "切断中"
                else -> "DISCONNECTED"
            }
            "vpn_status_error" -> when (code) {
                "fa" -> "اتصال برقرار نشد. لطفاً اینترنت خود را بررسی کرده یا سرور دیگری را انتخاب کنید."
                "ar" -> "فشل في الاتصال. يرجى التحقق من اتصال الإنترنت أو اختيار خادم آخر"
                "es" -> "Error de conexión. Verifique su conexión o seleccione otro servidor"
                "tr" -> "Bağlantı hatası. Lütfen internetinizi kontrol edin veya başka bir sunucu seçin"
                "de" -> "Verbindungsfehler. Bitte überprüfen Sie Ihr Internet oder wählen Sie einen anderen Server"
                "fr" -> "Erreur de connexion. Veuillez vérifier votre connexion ou choisir un autre serveur"
                "it" -> "Errore di connessione. Controlla la tua connessione o seleziona un altro server"
                "ru" -> "Ошибка подключения. Проверьте интернет или выберите другой сервер"
                "ja" -> "接続エラー。接続を確認するか、別のサーバーを選択してください"
                else -> "Connection failed. Please check your internet connection."
            }

            // 5. Connection / Disconnection Modals
            "connecting_title" -> when (code) {
                "fa" -> "در حال اتصال به امن‌ترین سرور..."
                "ar" -> "جارٍ الاتصال بأكثر الخوادم أماناً..."
                "es" -> "Conectando al servidor más seguro..."
                "tr" -> "En güvenli sunucuya bağlanılıyor..."
                "de" -> "Verbindung zum sichersten Server wird hergestellt..."
                "fr" -> "Connexion au serveur le plus sécurisé..."
                "it" -> "Connessione al server più sicuro..."
                "ru" -> "Подключение к наиболее защищенному серверу..."
                "ja" -> "最も安全なサーバーに接続中..."
                else -> "Connecting to the most secure server..."
            }
            "disconnecting_title" -> when (code) {
                "fa" -> "در حال بستن ایمن ارتباط..."
                "ar" -> "جارٍ إغلاق الاتصال الآمن..."
                "es" -> "Cerrando la conexión de forma segura..."
                "tr" -> "Güvenli bağlantı kapatılıyor..."
                "de" -> "Sichere Verbindung wird geschlossen..."
                "fr" -> "Fermeture sécurisée de la connexion..."
                "it" -> "Chiusura sicura della connessione..."
                "ru" -> "Безопасное закрытие соединения..."
                "ja" -> "安全に接続を切断中..."
                else -> "Closing secure connection..."
            }
            "please_wait" -> when (code) {
                "fa" -> "لطفاً منتظر بمانید..."
                "ar" -> "يرجى الانتظار..."
                "es" -> "Por favor espere..."
                "tr" -> "Lütfen bekleyin..."
                "de" -> "Bitte warten..."
                "fr" -> "Veuillez patienter..."
                "it" -> "Attendere prego..."
                "ru" -> "Пожалуйста, подождите..."
                "ja" -> "お待ちください..."
                else -> "Please wait..."
            }
            "cancel" -> when (code) {
                "fa" -> "لغو"
                "ar" -> "إلغاء"
                "es" -> "Cancelar"
                "tr" -> "İptal"
                "de" -> "Abbrechen"
                "fr" -> "Annuler"
                "it" -> "Annulla"
                "ru" -> "Отмена"
                "ja" -> "キャンセル"
                else -> "Cancel"
            }
            "cancel_connection" -> when (code) {
                "fa" -> "لغو اتصال"
                "ar" -> "إلغاء الاتصال"
                "es" -> "Cancelar conexión"
                "tr" -> "Bağlantıyı İptal Et"
                "de" -> "Verbindung abbrechen"
                "fr" -> "Annuler la connexion"
                "it" -> "Annulla connessione"
                "ru" -> "Отменить подключение"
                "ja" -> "接続をキャンセル"
                else -> "Cancel Connection"
            }

            // 6. Disconnect Confirm Dialog
            "disconnect_dialog_title" -> when (code) {
                "fa" -> "قطع اتصال VPN"
                "ar" -> "قطع اتصال VPN"
                "es" -> "Desconectar VPN"
                "tr" -> "VPN Bağlantısını Kes"
                "de" -> "VPN trennen"
                "fr" -> "Déconnecter le VPN"
                "it" -> "Disconnetti VPN"
                "ru" -> "Отключить VPN"
                "ja" -> "VPNを切断"
                else -> "Disconnect VPN"
            }
            "disconnect_dialog_msg" -> when (code) {
                "fa" -> "آیا مطمئن هستید که می‌خواهید اتصال را قطع کنید؟"
                "ar" -> "هل أنت متأكد من رغبتك في قطع الاتصال؟"
                "es" -> "¿Está seguro de que desea desconectarse de la VPN?"
                "tr" -> "VPN bağlantısını kesmek istediğinizden emin misiniz?"
                "de" -> "Sind Sie sicher, dass Sie die VPN-Verbindung trennen möchten?"
                "fr" -> "Êtes-vous sûr de vouloir vous déconnecter du VPN ?"
                "it" -> "Sei sicuro di voler disconnettere la VPN?"
                "ru" -> "Вы уверены, что хотите отключиться от VPN?"
                "ja" -> "VPNから切断してもよろしいですか？"
                else -> "Are you sure you want to disconnect from the VPN?"
            }
            "confirm_disconnect" -> when (code) {
                "fa" -> "تأیید"
                "ar" -> "قطع الاتصال"
                "es" -> "Desconectar"
                "tr" -> "Bağlantıyı Kes"
                "de" -> "Trennen"
                "fr" -> "Déconnecter"
                "it" -> "Disconnetti"
                "ru" -> "Отключить"
                "ja" -> "切断する"
                else -> "Disconnect"
            }

            // 7. Server Selection & Locations
            "select_location" -> when (code) {
                "fa" -> "انتخاب مکان"
                "ar" -> "تحديد الموقع"
                "es" -> "Seleccionar Ubicación"
                "tr" -> "Konum Seçin"
                "de" -> "Standort wählen"
                "fr" -> "Sélectionner un serveur"
                "it" -> "Seleziona Posizione"
                "ru" -> "Выбрать локацию"
                "ja" -> "ロケーションを選択"
                else -> "Select Location"
            }
            "selected_server_location" -> when (code) {
                "fa" -> "لوکیشن سرور انتخابی"
                "ar" -> "موقع الخادم المحدد"
                "es" -> "Ubicación del Servidor Seleccionado"
                "tr" -> "Seçilen Sunucu Konumu"
                "de" -> "Ausgewählter Serverstandort"
                "fr" -> "Emplacement du serveur sélectionné"
                "it" -> "Posizione Server Selezionato"
                "ru" -> "Выбранная локация сервера"
                "ja" -> "選択中のサーバー"
                else -> "Selected Server Location"
            }
            "change_server" -> when (code) {
                "fa" -> "تغییر سرور"
                "ar" -> "تغيير"
                "es" -> "Cambiar"
                "tr" -> "Değiştir"
                "de" -> "Ändern"
                "fr" -> "Changer"
                "it" -> "Modifica"
                "ru" -> "Сменить"
                "ja" -> "変更"
                else -> "Change"
            }
            "available_locations" -> when (code) {
                "fa" -> "سرورهای موجود"
                "ar" -> "الخوادم المتاحة"
                "es" -> "Ubicaciones Disponibles"
                "tr" -> "Kullanılabilir Konumlar"
                "de" -> "Verfügbare Standorte"
                "fr" -> "Serveurs disponibles"
                "it" -> "Posizioni Disponibili"
                "ru" -> "Доступные локации"
                "ja" -> "利用可能なサーバー"
                else -> "Available Locations"
            }
            "high_speed_locations" -> when (code) {
                "fa" -> "سرور پرسرعت اختصاصی"
                "ar" -> "خوادم فائقة السرعة"
                "es" -> "Ubicaciones de Alta Velocidad"
                "tr" -> "Yüksek Hızlı Konumlar"
                "de" -> "Hochgeschwindigkeits-Standorte"
                "fr" -> "Serveurs haute vitesse"
                "it" -> "Posizioni ad Alta Velocità"
                "ru" -> "Высокоскоростные серверы"
                "ja" -> "高速サーバー"
                else -> "High-Speed Locations"
            }
            "wireguard_protocol" -> when (code) {
                "fa" -> "پروتکل امن WireGuard®"
                "ar" -> "بروتوكول WireGuard® الآمن"
                "es" -> "Protocolo Seguro WireGuard®"
                "tr" -> "Güvenli WireGuard® Protokolü"
                "de" -> "Sicheres WireGuard®-Protokoll"
                "fr" -> "Protocole sécurisé WireGuard®"
                "it" -> "Protocollo Sicuro WireGuard®"
                "ru" -> "Безопасный протокол WireGuard®"
                "ja" -> "安全な WireGuard® プロトコル"
                else -> "WireGuard® Protocol"
            }

            // 8. Navigation & Tabs
            "tab_home" -> when (code) {
                "fa" -> "خانه"
                "ar" -> "الرئيسية"
                "es" -> "Inicio"
                "tr" -> "Ana Sayfa"
                "de" -> "Start"
                "fr" -> "Accueil"
                "it" -> "Home"
                "ru" -> "Главная"
                "ja" -> "ホーム"
                else -> "Home"
            }
            "tab_settings" -> when (code) {
                "fa" -> "تنظیمات"
                "ar" -> "الإعدادات"
                "es" -> "Ajustes"
                "tr" -> "Ayarlar"
                "de" -> "Einstellungen"
                "fr" -> "Paramètres"
                "it" -> "Impostazioni"
                "ru" -> "Настройки"
                "ja" -> "設定"
                else -> "Settings"
            }
            "settings" -> when (code) {
                "fa" -> "تنظیمات"
                "ar" -> "الإعدادات"
                "es" -> "Ajustes"
                "tr" -> "Ayarlar"
                "de" -> "Einstellungen"
                "fr" -> "Paramètres"
                "it" -> "Impostazioni"
                "ru" -> "Настройки"
                "ja" -> "設定"
                else -> "Settings"
            }

            // 9. Settings Sections & Items
            "general_support" -> when (code) {
                "fa" -> "عمومی و ارتباطات"
                "ar" -> "عام والدعم"
                "es" -> "General y Soporte"
                "tr" -> "Genel ve Destek"
                "de" -> "Allgemein & Support"
                "fr" -> "Général et assistance"
                "it" -> "Generale e Supporto"
                "ru" -> "Общие и поддержка"
                "ja" -> "一般・サポート"
                else -> "General & Support"
            }
            "share_app" -> when (code) {
                "fa" -> "اشتراک‌گذاری برنامه"
                "ar" -> "مشاركة التطبيق"
                "es" -> "Compartir Aplicación"
                "tr" -> "Uygulamayı Paylaş"
                "de" -> "App teilen"
                "fr" -> "Partager l'application"
                "it" -> "Condividi App"
                "ru" -> "Поделиться приложением"
                "ja" -> "アプリを共有"
                else -> "Share App"
            }
            "contact_us" -> when (code) {
                "fa" -> "ارتباط با ما"
                "ar" -> "اتصل بنا"
                "es" -> "Contáctenos"
                "tr" -> "Bize Ulaşın"
                "de" -> "Kontakt"
                "fr" -> "Contactez-nous"
                "it" -> "Contattaci"
                "ru" -> "Связаться с нами"
                "ja" -> "お問い合わせ"
                else -> "Contact Us"
            }
            "about_us" -> when (code) {
                "fa" -> "درباره ما"
                "ar" -> "حول التطبيق"
                "es" -> "Acerca de nosotros"
                "tr" -> "Hakkımızda"
                "de" -> "Über uns"
                "fr" -> "À propos"
                "it" -> "Chi siamo"
                "ru" -> "О нас"
                "ja" -> "アプリについて"
                else -> "About Us"
            }
            "security_connection" -> when (code) {
                "fa" -> "امنیت و اتصال"
                "ar" -> "الأمان والاتصال"
                "es" -> "Seguridad y Conexión"
                "tr" -> "Güvenlik ve Bağlantı"
                "de" -> "Sicherheit & Verbindung"
                "fr" -> "Sécurité et connexion"
                "it" -> "Sicurezza e Connessione"
                "ru" -> "Безопасность и подключение"
                "ja" -> "セキュリティと接続"
                else -> "Security & Connection"
            }
            "kill_switch" -> when (code) {
                "fa" -> "کیل سوئیچ (Kill Switch)"
                "ar" -> "مفتاح القفل (Kill Switch)"
                "es" -> "Interruptor de parada (Kill Switch)"
                "tr" -> "Kill Switch"
                "de" -> "Kill Switch"
                "fr" -> "Kill Switch"
                "it" -> "Kill Switch"
                "ru" -> "Kill Switch (Аварийная блокировка)"
                "ja" -> "キルスイッチ (Kill Switch)"
                else -> "Kill Switch"
            }
            "kill_switch_desc" -> when (code) {
                "fa" -> "قطع اینترنت در صورت قطعی VPN"
                "ar" -> "حظر الإنترنت في حال انقطاع VPN"
                "es" -> "Bloquear internet si se corta la VPN"
                "tr" -> "VPN koparsa interneti engelle"
                "de" -> "Internet bei VPN-Trennung blockieren"
                "fr" -> "Bloquer Internet en cas de coupure VPN"
                "it" -> "Blocca internet se la VPN si disconnette"
                "ru" -> "Блокировать интернет при обрыве VPN"
                "ja" -> "VPN切断時に通信を自動遮断"
                else -> "Block internet on disconnect"
            }
            "dns_leak_protect" -> when (code) {
                "fa" -> "محافظت DNS ضد فیلتر"
                "ar" -> "حماية من تسرب DNS"
                "es" -> "Protección contra fugas de DNS"
                "tr" -> "DNS Sızıntı Koruması"
                "de" -> "DNS-Leckschutz"
                "fr" -> "Protection contre les fuites DNS"
                "it" -> "Protezione da Perdite DNS"
                "ru" -> "Защита от утечек DNS"
                "ja" -> "DNS漏洩保護"
                else -> "DNS Leak Protection"
            }
            "dns_leak_desc" -> when (code) {
                "fa" -> "رمزنگاری تمام کوئری‌های DNS"
                "ar" -> "تشفير جميع استعلامات DNS عبر 1.1.1.1"
                "es" -> "Consultas DNS cifradas (1.1.1.1)"
                "tr" -> "Şifreli 1.1.1.1 DNS sorguları"
                "de" -> "Verschlüsselte 1.1.1.1 DNS-Anfragen"
                "fr" -> "Requêtes DNS chiffrées 1.1.1.1"
                "it" -> "Query DNS crittografate 1.1.1.1"
                "ru" -> "Зашифрованные DNS-запросы (1.1.1.1)"
                "ja" -> "暗号化された1.1.1.1 DNSクエリ"
                else -> "Encrypted 1.1.1.1 DNS queries"
            }
            "preferences" -> when (code) {
                "fa" -> "ترجیحات"
                "ar" -> "التفضيلات"
                "es" -> "Preferencias"
                "tr" -> "Tercihler"
                "de" -> "Einstellungen"
                "fr" -> "Préférences"
                "it" -> "Preferenze"
                "ru" -> "Настройки интерфейса"
                "ja" -> "設定・環境"
                else -> "Preferences"
            }
            "change_language" -> when (code) {
                "fa" -> "تغییر زبان"
                "ar" -> "تغيير اللغة"
                "es" -> "Cambiar Idioma"
                "tr" -> "Dili Değiştir"
                "de" -> "Sprache ändern"
                "fr" -> "Changer de langue"
                "it" -> "Cambia Lingua"
                "ru" -> "Сменить язык"
                "ja" -> "言語変更"
                else -> "Change Language"
            }
            "privacy_policy" -> when (code) {
                "fa" -> "سیاست حفظ حریم خصوصی"
                "ar" -> "سياسة الخصوصية"
                "es" -> "Política de Privacidad"
                "tr" -> "Gizlilik Politikası"
                "de" -> "Datenschutzrichtlinie"
                "fr" -> "Politique de confidentialité"
                "it" -> "Informativa sulla Privacy"
                "ru" -> "Политика конфиденциальности"
                "ja" -> "プライバシーポリシー"
                else -> "Privacy Policy"
            }
            "about_title" -> when (code) {
                "fa" -> "درباره VectaVPN"
                "ar" -> "حول VectaVPN"
                "es" -> "Acerca de VectaVPN"
                "tr" -> "VectaVPN Hakkında"
                "de" -> "Über VectaVPN"
                "fr" -> "À propos de VectaVPN"
                "it" -> "Informazioni su VectaVPN"
                "ru" -> "О VectaVPN"
                "ja" -> "VectaVPN について"
                else -> "About VectaVPN"
            }
            "about_desc" -> when (code) {
                "fa" -> "اپلیکیشن VectaVPN ارائه‌دهنده تونل‌های اختصاصی با رمزنگاری پیشرفته ChaCha20 و پروتکل‌های فوق سریع است.\n\nما متعهد به خط‌مشی Zero-Log (عدم ثبت گزارش فعالیت) هستیم و حریم خصوصی شما همواره محفوظ است."
                "ar" -> "يوفر تطبيق VectaVPN أنفاقًا مخصصة بتشفير ChaCha20 المتقدم وبروتوكولات فائقة السرعة مع التزام تام بسياسة عدم حفظ السجلات (Zero-Log)."
                "es" -> "VectaVPN proporciona túneles dedicados con cifrado ChaCha20 y protocolos ultra rápidos con estricta política de Cero Registros (Zero-Log)."
                "tr" -> "VectaVPN, gelişmiş ChaCha20 şifrelemesi ve ultra hızlı protokollerle Sıfır Kayıt (Zero-Log) politikası altında güvenli tüneller sağlar."
                "de" -> "VectaVPN bietet hochsichere Tunnel mit moderner ChaCha20-Verschlüsselung und strikter No-Log-Richtlinie für maximale Privatsphäre."
                "fr" -> "VectaVPN fournit des tunnels sécurisés avec chiffrement ChaCha20 et une politique stricte de non-conservation des journaux (Zero-Log)."
                "it" -> "VectaVPN offre tunnel dedicati con crittografia ChaCha20 e una rigorosa politica di No-Log per garantire la massima privacy."
                "ru" -> "VectaVPN предоставляет защищенные туннели с шифрованием ChaCha20 и строгой политикой отсутствия логов (Zero-Log)."
                "ja" -> "VectaVPN は、高度な ChaCha20 暗号化と超高速プロトコルを備えた厳格なノーログ (Zero-Log) トンネルを提供します。"
                else -> "VectaVPN provides high-speed, military-grade encrypted tunnels with strict Zero-Log privacy protection worldwide."
            }
            "got_it" -> when (code) {
                "fa" -> "متوجه شدم"
                "ar" -> "حسنًا"
                "es" -> "Entendido"
                "tr" -> "Anladım"
                "de" -> "Verstanden"
                "fr" -> "Compris"
                "it" -> "Ho capito"
                "ru" -> "Понятно"
                "ja" -> "了解"
                else -> "Got It"
            }

            // 10. Traffic Stats & Metrics
            "download_speed" -> when (code) {
                "fa" -> "دانلود"
                "ar" -> "تنزيل"
                "es" -> "Descarga"
                "tr" -> "İndirme"
                "de" -> "Download"
                "fr" -> "Téléchargement"
                "it" -> "Download"
                "ru" -> "Скачивание"
                "ja" -> "受信"
                else -> "Download"
            }
            "upload_speed" -> when (code) {
                "fa" -> "آپلود"
                "ar" -> "رفع"
                "es" -> "Subida"
                "tr" -> "Yükleme"
                "de" -> "Upload"
                "fr" -> "Envoi"
                "it" -> "Upload"
                "ru" -> "Загрузка"
                "ja" -> "送信"
                else -> "Upload"
            }
            "total_in" -> when (code) {
                "fa" -> "دریافت شده:"
                "ar" -> "الوارد:"
                "es" -> "Recibido:"
                "tr" -> "Gelen:"
                "de" -> "Empfangen:"
                "fr" -> "Reçu :"
                "it" -> "Ricevuto:"
                "ru" -> "Принято:"
                "ja" -> "受信合計:"
                else -> "Total In:"
            }
            "total_out" -> when (code) {
                "fa" -> "ارسال شده:"
                "ar" -> "الصادر:"
                "es" -> "Enviado:"
                "tr" -> "Giden:"
                "de" -> "Gesendet:"
                "fr" -> "Envoyé :"
                "it" -> "Inviato:"
                "ru" -> "Отправлено:"
                "ja" -> "送信合計:"
                else -> "Total Out:"
            }
            "live" -> when (code) {
                "fa" -> "زنده"
                "ar" -> "مباشر"
                "es" -> "EN VIVO"
                "tr" -> "CANLI"
                "de" -> "LIVE"
                "fr" -> "EN DIRECT"
                "it" -> "LIVE"
                "ru" -> "АКТИВЕН"
                "ja" -> "接続中"
                else -> "LIVE"
            }
            "idle" -> when (code) {
                "fa" -> "غیرفعال"
                "ar" -> "خامل"
                "es" -> "INACTIVO"
                "tr" -> "BOŞTA"
                "de" -> "INAKTIV"
                "fr" -> "INACTIF"
                "it" -> "INATTIVO"
                "ru" -> "ОЖИДАНИЕ"
                "ja" -> "待機中"
                else -> "IDLE"
            }

            // 11. Communication & Sharing
            "share_text" -> when (code) {
                "fa" -> "امنیت و اتصال پرسرعت را با VectaVPN تجربه کنید!\nhttps://play.google.com/store/apps/details?id=com.example"
                "ar" -> "استمتع باتصال آمن وفائق السرعة مع VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
                "es" -> "¡Disfruta de una conexión segura y de alta velocidad con VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
                "tr" -> "VectaVPN ile güvenli ve yüksek hızlı internetin tadını çıkarın!\nhttps://play.google.com/store/apps/details?id=com.example"
                "de" -> "Erleben Sie sicheres und schnelles Internet mit VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
                "fr" -> "Profitez d'une connexion sécurisée et ultra rapide avec VectaVPN !\nhttps://play.google.com/store/apps/details?id=com.example"
                "it" -> "Goditi una connessione sicura e veloce con VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
                "ru" -> "Быстрое и безопасное подключение с VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
                "ja" -> "VectaVPN で安全かつ高速なインターネット接続を体験してください！\nhttps://play.google.com/store/apps/details?id=com.example"
                else -> "Enjoy secure and high-speed browsing with VectaVPN!\nhttps://play.google.com/store/apps/details?id=com.example"
            }
            "email_subject" -> when (code) {
                "fa" -> "پشتیبانی VectaVPN"
                "ar" -> "دعم VectaVPN"
                "es" -> "Soporte VectaVPN"
                "tr" -> "VectaVPN Destek"
                "de" -> "VectaVPN Support"
                "fr" -> "Assistance VectaVPN"
                "it" -> "Supporto VectaVPN"
                "ru" -> "Поддержка VectaVPN"
                "ja" -> "VectaVPN サポート"
                else -> "VectaVPN Support"
            }
            "error_connection_msg" -> when (code) {
                "fa" -> "سرور پاسخ نمی‌دهد یا آفلاین است. لطفاً سرور دیگری را انتخاب کنید."
                "ar" -> "الخادم لا يستجيب أو غير متصل. يرجى اختيار خادم آخر."
                "es" -> "El servidor no responde o está desconectado. Seleccione otro servidor."
                "tr" -> "Sunucu yanıt vermiyor veya çevrimdışı. Lütfen başka bir sunucu seçin."
                "de" -> "Server antwortet nicht oder ist offline. Bitte wählen Sie einen anderen Server."
                "fr" -> "Le serveur ne répond pas ou est hors ligne. Veuillez choisir un autre serveur."
                "it" -> "Il server non risponde o è offline. Seleziona un altro server."
                "ru" -> "Сервер не отвечает или находится в автономном режиме. Выберите другой сервер."
                "ja" -> "サーバーが応答しないかオフラインです。別のサーバーを選択してください。"
                else -> "Server is not responding or offline. Please select another server."
            }
            "session_limit_title" -> when (code) {
                "fa" -> "اتمام زمان اتصال (۱:۳۰ ساعت)"
                "ar" -> "انتهاء وقت الجلسة (1:30 ساعة)"
                "es" -> "Límite de sesión alcanzado (1h 30m)"
                "tr" -> "Oturum Süresi Doldu (1s 30dk)"
                "de" -> "Sitzungslimit erreicht (1 Std. 30 Min.)"
                "fr" -> "Limite de session atteinte (1h 30m)"
                "it" -> "Limite di sessione raggiunto (1h 30m)"
                "ru" -> "Лимит сессии исчерпан (1ч 30м)"
                "ja" -> "セッション制限時間終了 (1時間30分)"
                else -> "Session Limit Reached (1h 30m)"
            }
            "session_limit_desc" -> when (code) {
                "fa" -> "مدت زمان مجاز اتصال پیوسته (۱ ساعت و نیم) به پایان رسید و اتصال قطع شد. برای اتصال مجدد ضربه بزنید."
                "ar" -> "انتهت فترة الاتصال المسموح بها (ساعة ونصف) وتم قطع الاتصال. انقر لإعادة الاتصال."
                "es" -> "Se alcanzó el límite de 1.5 horas de conexión continua. Toque para reconectarse."
                "tr" -> "1.5 saatlik kesintisiz bağlantı süresi doldu ve bağlantı kesildi. Yeniden bağlanmak için dokunun."
                "de" -> "Das Zeitlimit von 1,5 Stunden wurde erreicht. Tippen Sie auf Erneut verbinden."
                "fr" -> "La limite de connexion continue de 1h30 a été atteinte. Appuyez pour vous reconnecter."
                "it" -> "Il limite di sessione di 1,5 ore è stato raggiunto. Tocca per riconnetterti."
                "ru" -> "Время непрерывного соединения (1,5 часа) истекло. Нажмите для повторного подключения."
                "ja" -> "連続接続の制限時間（1時間30分）に達したため切断されました。再接続をタップしてください。"
                else -> "Your 1.5-hour connection time limit was reached and the session ended. Tap reconnect to connect again."
            }
            "reconnect" -> when (code) {
                "fa" -> "اتصال مجدد"
                "ar" -> "إعادة الاتصال"
                "es" -> "Reconectar"
                "tr" -> "Yeniden Bağlan"
                "de" -> "Erneut verbinden"
                "fr" -> "Reconnecter"
                "it" -> "Riconnetti"
                "ru" -> "Переподключить"
                "ja" -> "再接続"
                else -> "Reconnect"
            }
            "close" -> when (code) {
                "fa" -> "بستن"
                "ar" -> "إغلاق"
                "es" -> "Cerrar"
                "tr" -> "Kapat"
                "de" -> "Schließen"
                "fr" -> "Fermer"
                "it" -> "Chiudi"
                "ru" -> "Закрыть"
                "ja" -> "閉じる"
                else -> "Close"
            }

            else -> key
        }
    }

    /**
     * Translated Country / Server Name
     */
    fun getServerName(countryCode: String, lang: String): String {
        val code = lang.lowercase().trim()
        return when (countryCode.uppercase().trim()) {
            "US" -> when (code) {
                "fa" -> "ایالات متحده"
                "ar" -> "الولايات المتحدة"
                "es" -> "Estados Unidos"
                "tr" -> "Amerika Birleşik Devletleri"
                "de" -> "Vereinigte Staaten"
                "fr" -> "États-Unis"
                "it" -> "Stati Uniti"
                "ru" -> "США"
                "ja" -> "アメリカ合衆国"
                else -> "United States"
            }
            "DE" -> when (code) {
                "fa" -> "آلمان"
                "ar" -> "ألمانيا"
                "es" -> "Alemania"
                "tr" -> "Almanya"
                "de" -> "Deutschland"
                "fr" -> "Allemagne"
                "it" -> "Germania"
                "ru" -> "Германия"
                "ja" -> "ドイツ"
                else -> "Germany"
            }
            "GB" -> when (code) {
                "fa" -> "انگلستان"
                "ar" -> "المملكة المتحدة"
                "es" -> "Reino Unido"
                "tr" -> "Birleşik Krallık"
                "de" -> "Großbritannien"
                "fr" -> "Royaume-Uni"
                "it" -> "Regno Unito"
                "ru" -> "Великобритания"
                "ja" -> "イギリス"
                else -> "United Kingdom"
            }
            "NL" -> when (code) {
                "fa" -> "هلند"
                "ar" -> "هولندا"
                "es" -> "Países Bajos"
                "tr" -> "Hollanda"
                "de" -> "Niederlande"
                "fr" -> "Pays-Bas"
                "it" -> "Paesi Bassi"
                "ru" -> "Нидерланды"
                "ja" -> "オランダ"
                else -> "Netherlands"
            }
            "FR" -> when (code) {
                "fa" -> "فرانسه"
                "ar" -> "فرنسا"
                "es" -> "Francia"
                "tr" -> "Fransa"
                "de" -> "Frankreich"
                "fr" -> "France"
                "it" -> "Francia"
                "ru" -> "Франция"
                "ja" -> "フランス"
                else -> "France"
            }
            "CA" -> when (code) {
                "fa" -> "کانادا"
                "ar" -> "كندا"
                "es" -> "Canadá"
                "tr" -> "Kanada"
                "de" -> "Kanada"
                "fr" -> "Canada"
                "it" -> "Canada"
                "ru" -> "Канада"
                "ja" -> "カナダ"
                else -> "Canada"
            }
            "JP" -> when (code) {
                "fa" -> "ژاپن"
                "ar" -> "اليابان"
                "es" -> "Japón"
                "tr" -> "Japonya"
                "de" -> "Japan"
                "fr" -> "Japon"
                "it" -> "Giappone"
                "ru" -> "Япония"
                "ja" -> "日本"
                else -> "Japan"
            }
            "SG" -> when (code) {
                "fa" -> "سنگاپور"
                "ar" -> "سنغافورة"
                "es" -> "Singapur"
                "tr" -> "Singapur"
                "de" -> "Singapur"
                "fr" -> "Singapour"
                "it" -> "Singapore"
                "ru" -> "Сингапур"
                "ja" -> "シンガポール"
                else -> "Singapore"
            }
            else -> countryCode
        }
    }

    fun getPrivacyPolicy(lang: String): String {
        return when (lang.lowercase().trim()) {
            "fa" -> samplePrivacyPolicyPersian
            "ar" -> samplePrivacyPolicyArabic
            "es" -> samplePrivacyPolicySpanish
            "tr" -> samplePrivacyPolicyTurkish
            "de" -> samplePrivacyPolicyGerman
            "fr" -> samplePrivacyPolicyFrench
            "it" -> samplePrivacyPolicyItalian
            "ru" -> samplePrivacyPolicyRussian
            "ja" -> samplePrivacyPolicyJapanese
            else -> samplePrivacyPolicyEnglish
        }
    }

    val samplePrivacyPolicyPersian = """
        سیاست حفظ حریم خصوصی و شرایط خدمات VectaVPN
        
        ۱. خط‌مشی عدم ثبت وقایع (Strict No-Logs Policy):
        ما در VectaVPN به حریم خصوصی شما احترام کامل می‌گذاریم. سرورهای ما هیچ‌گونه لاگ یا داده‌ای از ترافیک وب، مقصد ارتباطی، داده‌های تبادل‌شده یا کوئری‌های DNS شما را جمع‌آوری، ذخیره یا به اشتراک نمی‌گذارند.
        
        ۲. رمزنگاری سرتاسری ترافیک:
        تمام داده‌های خروجی و ورودی دستگاه شما از طریق الگوریتم‌های رمزنگاری پیشرفته نظامی AES-256-GCM و پروتکل‌های مدرن ChaCha20/Poly1305 محافظت می‌شوند.
        
        ۳. حفاظت از هویت و آی‌پی:
        آدرس IP واقعی شما با آدرس سرور منتخب ماسک شده و از نشت اطلاعات مکانی و هویتی جلوگیری به عمل می‌آید.
        
        ۴. شرایط استفاده:
        استفاده از سرویس صرفاً جهت حفظ حریم خصوصی، ارتقای امنیت و دسترسی آزاد به وب ارائه می‌شود.
        
        با زدن دکمه «پذیرش و ادامه»، شما موافقت خود را با تمامی بندهای فوق اعلام می‌دارید.
    """.trimIndent()

    val samplePrivacyPolicyArabic = """
        سياسة الخصوصية وشروط خدمة VectaVPN
        
        ١. سياسة عدم الاحتفاظ بالسجلات (Strict No-Logs Policy):
        نحن في VectaVPN نحترم خصوصيتك بشكل كامل. لا تجمع خوادمنا أو تخزن أو تشارك أي سجلات لنشاطك، أو وجهات التصفح، أو استعلامات DNS.
        
        ٢. تشفير متقدم وشامل:
        تتم حماية جميع البيانات عبر خوارزميات التشفير AES-256-GCM وبروتوكول ChaCha20 للدفاع عن هويتك في الشبكات العامة.
        
        ٣. حجب عنوان IP الحقيقي:
        يتم استبدال عنوان IP الحقيقي الخاص بك ديناميكيًا بعنوان الخادم المحدد لمنع أي تسريب لمعلوماتك.
        
        ٤. شروط الاستخدام:
        توافق على استخدام التطبيق لحماية الخصوصية الشخصية وتصفح الإنترنت بأمان.
        
        بالنقر على «قبول ومتابعة»، فإنك تؤكد موافقتك على هذه الشروط.
    """.trimIndent()

    val samplePrivacyPolicySpanish = """
        Política de Privacidad y Términos de Servicio de VectaVPN
        
        1. Política Estricta de Cero Registros (No-Logs):
        En VectaVPN respetamos plenamente su privacidad. Nuestros servidores no inspeccionan, almacenan ni comparten su historial de navegación, marcas de tiempo, destinos ni consultas DNS.
        
        2. Cifrado Militar de Extremo a Extremo:
        Todo el tráfico entrante y saliente está protegido mediante algoritmos AES-256-GCM y ChaCha20/Poly1305 para proteger su identidad.
        
        3. Ocultamiento de Dirección IP:
        Su dirección IP real se oculta dinámicamente mediante el servidor seleccionado, protegiendo su ubicación e identidad.
        
        4. Términos de Uso:
        El servicio se proporciona exclusivamente para proteger su privacidad y garantizar una navegación segura y libre.
        
        Al tocar «Aceptar y Continuar», usted acepta todos los términos expuestos.
    """.trimIndent()

    val samplePrivacyPolicyTurkish = """
        VectaVPN Gizlilik Politikası ve Hizmet Şartları
        
        1. Sıfır Kayıt Politikası (Strict No-Logs):
        VectaVPN olarak gizliliğinize tam saygı duyuyoruz. Sunucularımız gezinme geçmişinizi, bağlantı zamanlarınızı, trafik hedeflerinizi veya DNS sorgularınızı kaydetmez veya paylaşmaz.
        
        2. Uçtan Uca Askeri Düzey Şifreleme:
        Tüm gelen ve giden veriler AES-256-GCM ve ChaCha20 şifreleme algoritmalarıyla korunur.
        
        3. Gerçek IP Gizleme:
        Gerçek IP adresiniz seçilen sunucunun adresi ile maskelenerek konum ve kimlik sızıntıları önlenir.
        
        4. Kullanım Şartları:
        Hizmet yalnızca kişisel gizliliği korumak ve güvenli internet erişimi sağlamak amacıyla sunulmaktadır.
        
        «Kabul Et ve Devam Et» düğmesine dokunarak şartları kabul etmiş olursunuz.
    """.trimIndent()

    val samplePrivacyPolicyGerman = """
        VectaVPN - Datenschutzrichtlinie & Nutzungsbedingungen
        
        1. Strikte No-Logs-Richtlinie:
        VectaVPN speichert oder teilt keinerlei Browserverlauf, Verbindungszeitstempel, Verkehrsdaten oder DNS-Abfragen.
        
        2. Ende-zu-Ende-Verschlüsselung:
        Ihr Datenverkehr wird durch moderne AES-256-GCM und ChaCha20-Verschlüsselung auf Militärstandard geschützt.
        
        3. IP-Maskierung:
        Ihre echte IP-Adresse wird dynamisch durch das Gateway des ausgewählten Servers ersetzt.
        
        4. Nutzungsbedingungen:
        Sie stimmen zu, VectaVPN ausschließlich zum Schutz der Privatsphäre und zur sicheren Datenübertragung zu nutzen.
        
        Mit dem Tippen auf «Akzeptieren & Weiter» stimmen Sie diesen Bedingungen zu.
    """.trimIndent()

    val samplePrivacyPolicyFrench = """
        Politique de confidentialité et conditions de VectaVPN
        
        1. Politique stricte de non-conservation des journaux (No-Logs) :
        VectaVPN respecte votre vie privée. Nos serveurs n'enregistrent, ne stockent ni ne partagent aucun historique de navigation, destination ou requête DNS.
        
        2. Chiffrement de niveau militaire :
        Toutes les communications entrantes et sortantes sont protégées par les chiffrements AES-256-GCM et ChaCha20.
        
        3. Masquage de l'adresse IP :
        Votre adresse IP réelle est masquée par celle du serveur sélectionné pour garantir votre anonymat.
        
        4. Conditions d'utilisation :
        L'utilisation du service est dédiée à la sécurité personnelle et à la protection de la vie privée.
        
        En appuyant sur « Accepter et continuer », vous acceptez ces conditions.
    """.trimIndent()

    val samplePrivacyPolicyItalian = """
        Informativa sulla Privacy e Termini di Servizio di VectaVPN
        
        1. Politica No-Log Rigorosa:
        VectaVPN rispetta pienamente la tua privacy. I nostri server non registrano né condividono la cronologia di navigazione, i timestamp o le query DNS.
        
        2. Crittografia di Grado Militare:
        Tutto il traffico è protetto tramite avanzati algoritmi AES-256-GCM e ChaCha20.
        
        3. Mascheramento dell'IP:
        Il tuo vero indirizzo IP viene sostituito con quello del server selezionato per garantire il completo anonimato.
        
        4. Condizioni d'uso:
        Il servizio è offerto esclusivamente per la protezione della sicurezza e della privacy personale.
        
        Toccando «Accetta e Continua», confermi di accettare questi termini.
    """.trimIndent()

    val samplePrivacyPolicyRussian = """
        Политика конфиденциальности и условия использования VectaVPN
        
        1. Строгая политика отсутствия логов (No-Logs):
        VectaVPN полностью уважает вашу конфиденциальность. Мы не собираем, не храним и не передаем историю посещений, временные метки или DNS-запросы.
        
        2. Сквозное шифрование военного уровня:
        Весь входящий и исходящий трафик защищен передовыми шифрами AES-256-GCM и ChaCha20.
        
        3. Маскировка реального IP:
        Ваш настоящий IP-адрес скрывается адресом выбранного защищенного сервера.
        
        4. Условия использования:
        Сервис предназначен исключительно для защиты персональных данных и безопасного серфинга в сети.
        
        Нажимая «Принять и продолжить», вы соглашаетесь со всеми условиями.
    """.trimIndent()

    val samplePrivacyPolicyJapanese = """
        VectaVPN プライバシーポリシーおよび利用規約
        
        1. 厳格なノーログポリシー (Strict No-Logs):
        VectaVPN はお客様のプライバシーを最重要視しています。閲覧履歴、接続タイムスタンプ、DNSクエリの記録や共有は一切行いません。
        
        2. 軍用規格のエンドツーエンド暗号化:
        すべての通信は強力な AES-256-GCM および ChaCha20 暗号化アルゴリズムにより保護されます。
        
        3. IPアドレスの完全なマスキング:
        実際のパブリックIPアドレスは選択されたサーバーのものに動的に置換され、位置情報の漏洩を防止します。
        
        4. 利用規約:
        当サービスは個人のプライバシー保護および安全なネットワーク接続のためにのみ提供されます。
        
        「同意して続行」をタップすることで、上記規約に同意したことになります。
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
