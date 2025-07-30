# Specification: KAP Bildirim Testi

## Scenario: QNB bildirimi için detaylı arama ve Excel kontrolü
* "https://www.kap.org.tr/tr" adresine gidilir
* Sayfanın URL'si "https://www.kap.org.tr/tr" olmalı
* "menuBtn_BildirimSorgulari" menüsüne tıklanır
* "submenuLink_DetayliSorgulama" seçilir
* Sayfanın URL'si "https://www.kap.org.tr/tr/bildirim-sorgu" olmalı
* "tabBtn_SirketBildirimi" sekmesi seçilir
* "dropdownToggle_SirketGrubu" açılır menüsü tıklanır
* "İşlem Gören Şirketler" grubu açılır menüden seçilir
* Tarih olarak "01.06.2025" girilir
* "button_Ara" butonuna tıklanır
* "QNB" içeren ilk satıra tıklanır
* Sayfanın URL'si "Bildirim" içermeli
* "excel" dosyası indirilir
* "excel" dosyası "Bildirimler.xls" olarak indirilmiş olmalı


### Opsiyonel olarak KAP ana ve alt menüsündeki butonlar (ör. Şirketler, Fonlar, Bugün Gelen Bildirimler vb.) eklenmiştir. (Bkz. /elements/KapHomePage.json)
### Opsiyonel olarak bugün gelen bildirimler, beklenen bildirimler, kalem sorgulama eklenmiştir.(Bkz. /elements/KapHomePage.json)
### Opsiyonel olarak fon bildirim sekmesi de eklenmiştir. (Bkz. /elements/QueryPage.json)
### Dropdown menüden istenilen seçenek seçilebilmektedir. (Örn: Yatırım Kuruluşları, Portföy Yönetim Şirketleri...)
### Tarih seçimi dinamik olarak entegre edilmiştir.
### Şirket seçimi dinamik olarak entegre edilmiştir. (Örn: ZKBVK, AYDEM...)
### Opsiyonel olarak word dosyası da indirilebilir şekildedir. ("word", "word", "Bildirimler.doc")
