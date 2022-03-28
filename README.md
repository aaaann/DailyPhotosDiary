# DailyPhotosDiary
Приложение DailyPhotosDiary предназначено для ведения ежедневника с фотографиями. 

## Функции
- Для хранения изображений требуется авторизация с помощью электронной почты и пароля. После авторизации повторно не нужно вводить логопасс при каждом входе. При желании можно менять аккаунты.
- Можно добавить изображение из галереи или сделать фото с камеры. Опционально позволяет добавить описание к картинке. Требуется выбрать дату, за которой будет закреплено изображение (таким образом, можно загрузить изображение задним числом). При добавлении новой картинки по умолчанию ставится текущая дата.
- В ленте дневника изображения группируются по дате.
- По умолчанию группы фото сортируются по дате по убыванию. Сортировку можно изменить кнопкой в тулбаре. Настройка сохраняется до смены аккаунта.
- В настройках можно включить функцию ежедневных напоминаний о добавлении картинки. 
- Чтобы выйти из аккаунта, следует выбрать соответствующий пункт в настройках. 

## Экраны
![dpd_login](https://user-images.githubusercontent.com/32578297/160494804-48f5ea02-3ebd-4542-be85-c1dd76329b50.png)
![dpd_create_acc](https://user-images.githubusercontent.com/32578297/160494863-267e9617-9a7a-489b-a596-4a987f13a55e.png)
![dpd_drawer](https://user-images.githubusercontent.com/32578297/160494930-a04c84eb-6268-4d9e-baba-8fcd57b9da6a.png)
![dpd_gallery](https://user-images.githubusercontent.com/32578297/160494957-728e2978-f723-4e20-906e-6138e3403371.png)
![dpd_add_image](https://user-images.githubusercontent.com/32578297/160494980-b9e308be-c1c5-4303-ace7-fdc05133ffe9.png)
![dpd_add_image_full](https://user-images.githubusercontent.com/32578297/160495011-33e24b07-35fb-4e95-bb81-f08493b324c5.png)
![dpd_edit_image](https://user-images.githubusercontent.com/32578297/160495034-c794a0d4-366e-49c3-a171-9eb8e8b27328.png)
![dpd_settings](https://user-images.githubusercontent.com/32578297/160495075-dd55471c-b1db-4748-8da4-dd66e7f5a856.png)

## Tech stack
- Kotlin
- Kotlin Coroutines + Flow
- Navigation Component + safeargs
- Dagger2
- Jetpack Preferences DataStore
- Glide
- WorkManager
- Animated Vector Drawable
- Facebook Shimmer Android
- Firebase Authentication SDK
- Firebase Realtime Database SDK
- Firebase Cloud Storage SDK
- Android Lint, Detekt
- Kaspresso

## Архитектура
Постороено по MVVM + Clean Arch и принципу Single Activity App.

Используется модульная архитектура.
Модули делятся на три вида: **core**, **common**, **feature**.
Feature-модули подразделяются на **impl**- и **api**- для разделения внутренних реализаций и зависимостей, предоставляемых другим фичам.

