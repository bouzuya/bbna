# bbna: blog.bouzuya.net for Android

blog.bouzuya.net for Android.

<https://play.google.com/store/apps/details?id=net.bouzuya.blog>

## System Architecture Diagram

```mermaid
flowchart LR
  subgraph android
    frontend
  end

  subgraph google_cloud
    subgraph cloud_run["Cloud Run"]
      backend
    end

    scheduler["Cloud Scheduler"]

    db["Firestore"]
  end

  subgraph users
    user
    bouzuya
  end

  bbn["blog.bouzuya.net API"]
  expo["Expo Push API"]

  backend --> bbn
  backend --> db
  backend --> expo
  bouzuya .-> bbn
  frontend --> backend
  frontend --> bbn
  frontend --> expo
  scheduler --> backend
  user --> frontend
  expo .-> frontend
```

## UseCases

### Fetch entries

```mermaid
sequenceDiagram
  participant user
  participant frontend
  participant bbn as "blog.bouzuya.net API"

  user ->> frontend : Run
  Note over frontend : Register ExpoPushToken
  frontend ->> bbn : Fetch entries
  bbn -->> frontend : (entries)
```

### Register ExpoPushToken

```mermaid
sequenceDiagram
  participant user
  participant frontend
  participant backend
  participant db
  participant expo as "Expo Push API"

  user ->> frontend : Run
  frontend ->> frontend : Get permissions
  frontend ->> user : Request permissions
  user -->> frontend : Grant permissions
  frontend ->> expo : Create ExpoPushToken (expo-notifications)
  expo -->> frontend : (ExpoPushToken)
  frontend ->> backend : Register ExpoPushToken
  backend ->> db : Write ExpoPushToken
  backend -->> frontend : OK
```

### Send push notifications

```mermaid
sequenceDiagram
  participant scheduler
  participant backend
  participant db
  participant expo as "Expo Push API"
  participant bbn as "blog.bouzuya.net API"

  scheduler ->> backend : Run
  backend ->> bbn : Fetch entries
  bbn ->> backend : (entries)
  backend ->> backend : Check updates
  backend ->> db : Read ExpoPushTokens
  db -->> backend : (ExpoPushTokens)
  backend ->> expo : Send push notifications
  expo -->> backend : (ExpoPushTickets)
  backend ->> db : Write ExpoPushTickets
  backend -->> scheduler : OK
```

### Delete invalid ExpoPushTokens

```mermaid
sequenceDiagram
  participant scheduler
  participant backend
  participant db
  participant expo as "Expo Push API"

  scheduler ->> backend : Run (after 15+ mins)
  backend ->> db : Read ExpoPushTickets
  db -->> backend : (ExpoPushTickets)
  backend ->> expo : Get push notification receipts
  expo -->> backend : (ExpoPushReceipt)
  backend ->> db : Delete invalid ExpoPushTokens
  backend -->> scheduler : OK
```
