# PokedexApp

Modern bir Android Pokedex uygulaması. Pokemon listesini grid görünümünde sunar, detay ekranında tip, boyut, ağırlık ve animasyonlu istatistikleri gösterir.

---

## Demo

<img height="480" alt="Video Project 1 (1)" src="https://github.com/user-attachments/assets/55b8e88c-b15b-4b6a-a9b1-e7878b3319db" />


---

## Ekran Görüntüleri

| Liste Ekranı | Detay Ekranı | Base Stats |
|:---:|:---:|:---:|
| ![Liste](https://github.com/user-attachments/assets/88f9fc6a-51ca-4cbb-bfcd-8213b32352e6) | ![Detay](https://github.com/user-attachments/assets/315038c1-3bee-4064-8561-f5e0ec1e0647) | ![Stats](https://github.com/user-attachments/assets/e6bf3f54-cf77-4c85-a3fd-403b9bd8cfe4) |

---

## Özellikler

- **Pokemon listesi** — 2 sütunlu grid, sayfalama (pagination) ile sonsuz kaydırma
- **Pokemon detayı** — görsel, isim, tip rozetleri, boyut ve ağırlık bilgisi
- **Dinamik tema rengi** — Pokemon sprite'ından dominant renk çıkarımı (Palette API)
- **Base Stats bottom sheet** — animasyonlu istatistik çubukları (HP, ATK, DEF, SPD, S.ATK, S.DEF)
- **Hata yönetimi** — yükleme, retry, pagination hatalarında Snackbar
- **Ortam desteği** — development, staging, production product flavor'ları
- **Geliştirici araçları** — Chucker (debug), HTTP logging, Bearer token auth

---

## Teknoloji Stack

| Kategori | Teknoloji |
|----------|-----------|
| Dil | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Mimari | MVVM + Clean Architecture |
| DI | Hilt |
| Network | Retrofit, OkHttp |
| Görsel | Coil |
| Navigasyon | Navigation Compose |
| Async | Kotlin Coroutines, Flow |

---

## Mimari

Uygulama katmanlı Clean Architecture yapısında organize edilmiştir:

```
UI (Compose Screens + ViewModels)
        ↓
Domain (Models + Repository Interface)
        ↓
Data (Repository Impl + Remote API + DTOs)
```

**Veri akışı:** `ViewModel` → `Repository` → `ApiService` → API yanıtı `Resource<T>` wrapper ile UI state'e dönüştürülür.

---

## Ekranlar

### Pokemon Listesi
- API'den sayfalı Pokemon listesi çeker
- Her kart için detay isteği ile tip bilgisi alınır
- Sprite görselinden dominant renk ile kart arka planı renklendirilir
- Listenin sonuna yaklaşıldığında otomatik yükleme

### Pokemon Detayı
- Seçilen Pokemon'un detay bilgileri
- Top bar ve gradient arka plan, sprite rengine göre uyarlanır
- "Base Stats" butonu ile bottom sheet açılır/kapanır
- İstatistik çubukları sıralı animasyonla gösterilir

---

## Proje Yapısı

```
app/src/main/java/com/example/pokedexapp/
├── data/
│   ├── model/              # API response modelleri
│   ├── remote/             # ApiService, interceptor'lar
│   └── repository/         # Repository implementasyonu
├── domain/
│   ├── model/              # Domain modelleri
│   └── repository/         # Repository arayüzü
├── di/                     # Hilt modülleri
├── navigation/             # NavGraph, route tanımları
├── ui/
│   ├── screen/
│   │   ├── list/           # Liste ekranı + ViewModel
│   │   └── detail/         # Detay ekranı + ViewModel + bileşenler
│   └── theme/              # Renkler, tipografi, tema
├── util/                   # Constants, Resource, DominantColorExtractor
└── viewmodel/              # BaseViewModel
```

## API

Uygulama Pokemon API kullanır:

- `GET /pokemon?limit=&offset=` — Pokemon listesi
- `GET /pokemon/{id}` — Pokemon detayı

