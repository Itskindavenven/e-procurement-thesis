# Notification Service - Requirements

## Functional Requirements
1.  **Templating**: Must verify templates exist before sending. Fallback to generic text if template fails.
2.  **Reliability**: If Email server is down, should retry (Exponential Backoff).
3.  **Preferences**: Must respect user's "Unsubscribe" or "Do Not Disturb" settings.

## Non-Functional Requirements
1.  **Asynchronous**: Sending emails must happen in a background thread or queue worker to not block the consumer loop.
2.  **Rate Limiting**: Prevent spamming a single user (e.g., max 10 emails/hour).
3.  **Audit**: Log every delivery attempt (Success/Failure) for troubleshooting.

## Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.username=...
spring.mail.password=...
notification.retry.max-attempts=3
```
