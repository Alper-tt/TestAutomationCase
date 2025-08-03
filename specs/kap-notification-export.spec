# Specification: KAP Bildirim Testi

## Scenario: Masaüstü Web arayüzü için QNB bildirimi detaylı arama ve Excel kontrolü
* Tarayıcı boyutu "1920"x"1080" olarak ayarlanır
* "https://www.kap.org.tr/tr" adresine gidilir
* Sayfanın URL'si "https://www.kap.org.tr/tr" olmalı
* "menuBtn_BildirimSorgulari" elementine tıklanır
* "submenuLink_DetayliSorgulama" elementine tıklanır
* Sayfanın URL'si "https://www.kap.org.tr/tr/bildirim-sorgu" olmalı
* "tabBtn_SirketBildirimi" elementine tıklanır
* "dropdownToggle_SirketGrubu" elementine tıklanır
* "dropdownOption_Generic" locatöründen "İşlem Gören Şirketler" parametresiyle seçim yapılır
* Tarih olarak "01.06.2025" girilir
* "button_Ara" elementine JS ile tıklanır
* "QNB" içeren ilk satıra tıklanır
* Sayfanın URL'si "Bildirim" içermeli
* "btn_File_Download_Generic" locatöründen "excel" parametresiyle seçim yapılır
* "excel" dosyası "Bildirimler.xls" olarak indirilmiş olmalı
* XLS dosyasındaki veriler ile web sayfasındaki veriler karşılaştırılır "/Users/alper/Desktop/testautomationcase/target/downloads/Bildirimler.xls"


## Scenario: Mobil Web arayüzü için QNB bildirimi detaylı arama ve Excel kontrolü
* Tarayıcı boyutu "390"x"844" olarak ayarlanır
* "https://www.kap.org.tr/tr" adresine gidilir
* Sayfanın URL'si "https://www.kap.org.tr/tr" olmalı
* "btn_Hamburger" elementine tıklanır
* "menuBtn_BildirimSorgulari" elementine tıklanır
* "submenuLink_DetayliSorgulama" elementine tıklanır
* Sayfanın URL'si "https://www.kap.org.tr/tr/bildirim-sorgu" olmalı
* "tabBtn_SirketBildirimi" elementine tıklanır
* "dropdownToggle_SirketGrubu" elementine tıklanır
* "dropdownOption_Generic" locatöründen "İşlem Gören Şirketler" parametresiyle seçim yapılır
* Tarih olarak "01.06.2025" girilir
* "button_Ara" elementine JS ile tıklanır
* "QNB" içeren ilk satıra tıklanır
* Sayfanın URL'si "Bildirim" içermeli