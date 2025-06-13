# カジカシ
アプリURL；https://kajikashi.onrender.com/

現状、Expo Goを使ったアプリ配信となっています。
お手数ですが、GooglePley ,AppStoreからExpo Goをインストールしていただきアプリ内でQRコードを読み取ってください。
- [GooglPlay](https://play.google.com/store/apps/details?id=host.exp.exponent&referrer=www)
- [AppStore](https://itunes.apple.com/app/apple-store/id982107779)

いつかアプリ単体でストア配信できたらいいな・・・

クライアント側リポジトリ:https://github.com/naoto-nakamura-ac/kajikashiClient.git
- クライアント側アプリの構築はこちらのリポジトリを参考にしてください
# サービス概要
家族の中でお互いの家事を見える化しスコアとして算出するアプリです。
少しでも夫婦、家族間ですれ違いがなくなることを祈ってます。

# 使用技術
## Backend
- Kotlin
- SpringBoot
## FrontEnd
- ReactNative
- Gluestack-UI
- Expo
## Enviroment
- Render (API Server)
- Expo GO (MobileApp Deploy)
## DataBase
- Postgresql 14
# Setup
1. リポジトリをローカル環境にCloneする
    ```
    https://github.com/naoto-nakamura-ac/kajikashi.git
    ```
   - IntelliJ,Postgresはインストール、セットアップ済み前提
2. JavaSDKをインストールする
   ```
   AmazonCoretto 21.07 aarcg64 を選択
   ```
3. 環境変数を設定
   ```
   DB_URL=jdbc:postgresql://localhost:5432/kajikashi_db;
   DB_USER=user;
   SPRING_PROFILES_ACTIVE=dev
   ```
  - 本番環境（Renderとか）ではSPRING_PROFILES_ACTIVE=prodにする
  - IntelliJに環境変数を設定する場合は実行環境ごとに設定がいるため注意する
    - SpringBoot ,Gradle ,Test...
4. PostgresにDBを作成する
  - DB名は`kajikashi_db`
5. IntelliJでRunする
6. IntelliJでビルドもしくは ./gradle buildする

