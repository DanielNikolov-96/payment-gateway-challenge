- [Domain model decisions](#domain-model-decisions)
  * [Domain model property type changes](#domain-model-property-type-changes)
- [Validation decisions](#validation-decisions)
- [External system response-handling](#external-system-response-handling)
- [Metrics](#metrics)
- [Out-of-scope](#out-of-scope)

## Domain model decisions

Sanitizing/normalizing user input for higher conversion rate

* allowing whitespaces before and after each api property
* trade-off is data integrity loss, as user data isn't passed as-is
  to the acquirer. Here we aren't using real customer data, so opted
  for the sanitization approach

For a more complex project, I would had considered using value objects, to ensure immutability and encapsulate validation and formatting logic within those classes.
Moreover, a Currency object would also cover a wider currency range and their specific currency unit edge cases (e.g JPY, crypto)

### Domain model property type changes


| property     | old type | new type | reasoning                   |
| ------------ | -------- | -------- | --------------------------- |
| card_number  | Integer  | String   | handling zero-leading input |
| expiry_month | Integer  | String   | handling zero-leading input |
| cvv          | Integer  | String   | handling zero-leading input |
| amount       | int      | Long     | larger range                |

`expiry year` - assumed we only accept a full such and don't allow for 2-digit such to avoid ambiguity on exact expiry year.
If the acquirer was an actual such, I would had relaxed this as the acquirer can deduce exact year upon their internal checks for the card.
`amount` - if the flow involved precise computations, I would had used BigDecimal

Sensitive payment fields were masked within the `toString`
implementations to avoid breaching data privacy regulations

## Validation decisions

The requirements didn't explicitly share rejected payments need to be
stored for further querying by the customer. Due to the small scale of the project, I opted for storing them to satisfy the requirements.

However, in real-life project, I would question if storing a payment that never reached the acquirer brings customer value based on customer use cases (querying for auditory, analytics, regulatory and reconciliation purposes).

Successfully challenging this storage need can save
storage space in the long run.

## External system response-handling

If there is a mismatch between the API contract with the acquirer
and their actual validation, just like the validation failure case, the payment is rejected and acquirer doesn't process it at all.
The decision-making outlined in _Validation decisions_ section apply here, as is the case with the raised concerns.

However, upon unavailable external provider, there is no payment
and no authorization status so no persistence is occurring.
The client is presented with a status code and error message, prompting them to try again later
as an external component couldn't process their request
(without exposing our internals, ie which provider it is and that
a provider is currently unavailable)

## Metrics

Here are some of the key non-operational metrics I would track for this system:

* Request latency (p50, p95, p99)
* Request throughput (requests per second)
* Error rate (percentage of failed requests)
  * breakdown per status codes and categories (4xx vs 5xx)

External service ones omitted as a dummy deterministic service is our only external dependency

## Out-of-scope

* Idempotency - key in a payments system, but not considered here as no actual balance change as part of the system flow (and to preserve project simplicity)
* Data storage - omitted as per project requirements. Concurrency also unhandled in this simplified project (thus, no ConcurrentHashMap usage)
* Resiliency - omitted as a dummy deterministic service is our only external dependency. Thus, no inclusion of connection pooling, retries, circuit breaker, etc. to preserve project simplicity
* Security - omitted as no actual sensitive data is being processed, and to preserve project simplicity