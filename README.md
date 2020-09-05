# Api сайта mos.ru (госуслуги москвы)
```kotlin
               val mosRu = MosRu.create(login, password)
               val flats = mosRu.getFlats()
               val flatId = flats.first().flatId
               val epd=mosRu.getEpd(YearMonth.of(2020, 8), flat, EpdType.CURRENT)
               epd?.pdf
```