use axum::{routing, Router};

async fn get_root() -> &'static str {
    "OK"
}

pub fn route<T>() -> Router<T>
where
    T: Clone + Send + Sync + 'static,
{
    Router::new().route("/", routing::get(get_root))
}

#[cfg(test)]
mod tests {
    use axum::{
        body::Body,
        http::{Request, StatusCode},
    };
    use tower::ServiceExt as _;

    use crate::tests::ResponseExt as _;

    use super::*;

    #[tokio::test]
    async fn test_get_root() -> anyhow::Result<()> {
        let app = MockApp;
        let router = route().with_state(app);
        let request = Request::builder()
            .method("GET")
            .uri("/")
            .body(Body::empty())?;
        let response = router.oneshot(request).await?;
        assert_eq!(response.status(), StatusCode::OK);
        assert_eq!(response.into_body_string().await?, "OK");
        Ok(())
    }

    #[derive(Clone, Default)]
    struct MockApp;
}
