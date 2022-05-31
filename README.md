# JavaFX-Data-Visualization

## Abstract / Proje Özeti
Geliştirilen proje JavaFX kullanarak oluşturulan bir masaüstü uygulamasıdır. Bu uygulamada ara yüz üzerinden seçilen uygun formatta veri dosyaları okunup işlenerek hareketli çubuk ve çizgi grafiğine dönüştürülmektedir. Uygun formatta Title xLabel verileri olmalı ve ayrıca her bir veri şu bilgileri içermelidir: (year,name,country,value,category). Xml dosyasında bahsedilen bilgiler attributeler üzerinden çekildiği için sıranın önemi olmamaktadır ama txt dosyasında bu bilgiler (year,name,country,value,category) şeklinde sıralı olmalıdır. Çekilen bu verilen daha sonra yine ara yüz üzerinden seçilen çubuk grafik veya çizgi grafiğe dönüştürülmekte ve tarih bilgisine göre sıralanıp animasyon haline getirilmektedir. Animasyon sırasında verilen sıralanmakta ve kategori verisine göre renklendirilmektedir. Ayrıca verilerin ismi ve o anki değerleri de ekrana yazdırılmaktadır. Kullanıcı ara yüz üzerinden animasyonu durdurabilmekte, yeniden başlatabilmekte veya tamamen durdurabilmektedir.

## Software Design / Yazılım Tasarımı
Proje genel olarak 3 ana bölümden oluşmaktadır. Bunlar verilerin ayrıştırılması, uygun sınıflara ve veri yapılarına aktarılması ve son olarak kullanıcı ara yüzüdür.

### Verilerin ayrıştırılması:
Bu bölümde kullanıcının sadece ara yüz üzerinden dosya seçmesi yeterlidir. Dosyayı seçtikten sonra bir butona basarak verilerin ayrıştırılması işlemi gerçekleşmektedir. Verilerin dosya uzantısı Parser sınıfı tarafından otomatik olarak algılanmakta ve txt için Scanner, XML için DOM aracı kullanılarak ayrıştırılmaktadır. Ayrıştırılan verilen bir Arrayliste okunmakta daha sonra bu ham veri işlenmektedir. Ham veri işlenirken Insertion Sort benzeri bir algoritma tasarlanmıştır. Bir tarih verisi seçilmekte ve listenin geri kalanı taranmakta ve aynı tarih bilgisine sahip veriler bir araya toplanmaktadır. Algoritmanın zaman karmaşıklığı O(n2) olduğu için büyük veri dosyalarında program kısa süreliğine duraklamaktadır. Burada daha verimli bir algoritma tasarlanabilir.
Ham veri işlenirken tarih bilgisi bir LocalDate objesine dönüştürülmekte eğer sadece yıl bilgisi varsa örneğin 2018 LocalDate objesine 2018-1-1 olarak eklenmektedir eğer ay, gün bilgisi var ise bu bilgiler kullanılarak LocalDate objesine dönüştürülmektedir. Burada LocalDate sınıfının kullanmamızın sebei LocalDate sınıfı Comparable interface’ini implemente ettiği için daha sonra tarihler sıralanırken bize avantaj sağlayacaktır. Bu LocalDate objeleri aynı tarihi içeren verilerin olduğu bir Arraylist ile birlike key LocalDate, Value ise Arraylist olacak şekilde bir HashMap’e eklenmektedir. Burda Hashmap kullanmamızın sebebi LocalDate objelerini ilgili arraylist ile hata olmadan eşleştirmek yani veri güvenliği ve ayrıca HashMap’in get() metodunun sabit O(1) zaman karmaşıklığından faydalanmaktır. Daha sonra Parser sınıfı işlenmiş verinin tutulduğu HashMap’i getData() metodu ile döndermekte ve ayrıca okunan veri dosyasının title ve xLabel bilgisini de get metotları ile döndermektedir.

### Sınıflar ve Veri Yapıları
Projede ERUDM de paylaşılan sınıflar gerçekleştirilmiş ve bunlar haricinde 2 farklı sınıf kullanılmıştır bunlar Parser ve Record sınıflarıdır. Parser sınıfı verileri ayrıştırmadan Record sınıfı ise dosyadan okunan ham veriyi tutmadan sorumlu sınıflardır. BarChart sınıfı yapıcı metodunda bir arraylist kabul etmekte ve bu arraylisti Barlara dönüştürmektedir. BarChart sınıfının drawBarChart metodu ise kendisine verilen bir Canvas objesine grafiğin kendisini ve Bar’ları çizmeden sorumludur. Her Bar objesi Name, Category ve Value değerlerini tutmaktadır. Bar objeleri kategorilerine göre renklendirilirken Category verisi işlenmekte ve #hex türünden renk bildiren bir stringe dönüştürülmektedir böylece her bir bar kendi kategorisine göre renklendirilecektir. LineChart sınıfı da BarChart sınıfı ile aynı özelliklere sahiptir. Animasyon oluşturma işlemi Controller sınıfına verilmiştir. Animasyon oluşturulurken Timeline sınıfı kullanılmıştır. Timeline sınıfı KeyFrameler içermekte ve bu KeyFrameleri belirlenen aralıklarla ekranda göstermektedir. KeyFrameler oluşturulurken önce verinin tamamını kapsayacak şekilde her farklı tarih için BarChart obje array’i oluşturulmuş daha sonra bu array’in uzunluğu kadar dönecek bir döngü ile KeyFrameler lambda expression ile BarChart’ın drawBarChart metodu çağrılmıştır. Özetle her KeyFrame bir BarChart durumunu ekrana çizecek şekilde oluşturulmuştur.

### Ara yüz
Ara yüz tasarlanırken bütün sembollerin ve butonların kullanıcı için anlaşılabilir olması amaçlanmıştır. Ara yüzde Split Pane kullanılmıştır sağ taraf grafiğin çizileceği alan, sol taraf ise kullanıcının animasyonu ve programı kontrol edeceği kısımdır. Animasyonu başlatan, duraklatan veya durduran butonlarda yaygın kullanılan play, pause, stop sembolleri kullanılmıştır. Grafik türünü seçme işlemi ise ComboBox ile gerçekleşmektedir.

## Conclusion / Sonuç
Genel olarak ara yüz kullanılırken programın çalışması için gerekli olan bilgilerin veya işlemlerin yapılıp yapılmadığı kontrol edilmiş ve Alert objeleri ile kullanıcıya uyarılar gösterilmiştir. Proje geliştirilirken olabildiğince açıklayıcı yorum satırları da kullanılmasına dikkat edilmiştir. Bar objeleri kategorilerine göre renklendirilirken kendi tuttuğu Category String’i bir #hex değere dönüştürülerek renklendirilmiştir. Bu oldukça efektif bir yöntemdir farklı kategorileri verinin içinde aramaktan ve kategoriye özgü renk atamaktan bizi kurtarır. Ama şöyle bir dezavantajı vardır barlar farklı renklendirilse de renkler birbirlerine yakın tonlar olduğu için kategoriye göre ayrılmamış gibi gözükmekte. Bu durumda birbirinden daha farklı tonlarda renk üretecek şekilde algoritma iyileştirilmelidir. Projede URL’den dosya okuma ve çizgi grafik kısımlarını gerçekleştiremedik. Çizgi grafik çizdirme işleminde bir verinin sonraki aşamada alacağı değeri bulacak bir algoritma tasarlayamadığımız için çizgi grafiği oluşturamadık ama yine de Line ve LineChart sınıflarını konsept olarak kodladık ama çalışan bir çizgi grafik elde edemedik. Ortaya konulan program verileri okuyup ayrıştırabildiği, çubuk grafik animasyonu oluşturabildiği ve ara yüz ile kullanıcının programın kendisini ve animasyonu yönetmesine olanak sağladığı için hedeflenen amacın birkaç eksik hariç çoğuna hizmet etmektedir.

Verilen proje ödevi oldukça eğlenceli ve öğreticiydi ama test için daha fazla veri dosyası paylaşılabilirdi. Özellikle ekrana hiçbir hazır kütüphane kullanmadan çubuk grafik çizdirme kısmı en öğretici kısımdı. Verilerin ekrana sığacak şekilde bir sayı aralığına dönüştürülmesi, her çubuğun ekrana düzgün şekilde pozisyonlanması, kategori verisine göre renklendirilmesi projenin en öğretici kısımlarıydı.
