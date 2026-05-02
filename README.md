# GraphQL Subscription Client

This project is the **client application** for GraphQL subscriptions.

The main GraphQL app emits employee events whenever an employee is created, updated, or deleted. This client application subscribes to those events and consumes the records in real time.

## Related Projects

- Main GraphQL app: [graphql-springboot](https://github.com/venkatram1999/graphql-springboot)
- Subscription client app: [graphql-subscription-client](https://github.com/venkatram1999/graphql-subscription-client)

## What this project does

This client connects to the main GraphQL server using `WebSocketGraphQlClient` and listens for subscription events.

It subscribes to:
- `employeeCreated`
- `employeeUpdated`
- `employeeDeleted` 

When the main app emits an event, this client receives the employee data and prints it in the logs.

## How subscription works

### In the main app

The main GraphQL app contains employee mutations such as:
- `createEmployee`
- `updateEmployeeById`
- `deleteEmployee` 

When any of these operations are executed, `EmployeeService` updates the employee data and calls `EmployeeEventPublisher` to publish the event.

`EmployeeEventPublisher` emits data using reactive streams for:
- `employeeCreated`
- `employeeUpdated`
- `employeeDeleted` 

### In this client app

This client starts automatically and opens GraphQL subscriptions when the application runs.

The client uses `WebSocketGraphQlClient` configuration from `GraphQlClientConfig` to connect to the subscription server URL.

Then `EmployeeSubscriptionClient` subscribes to:
- `employeeCreated`
- `employeeUpdated`
- `employeeDeleted` 

Whenever the server emits a new employee event, this client consumes that record immediately.

## Subscription queries used in this client

### Employee Created

```graphql
subscription {
  employeeCreated {
    id
    name
    email
    department
  }
}
```

### Employee Updated

```graphql
subscription {
  employeeUpdated {
    id
    name
    email
    department
  }
}
```

### Employee Deleted

```graphql
subscription {
  employeeDeleted {
    id
    name
    email
    department
  }
}
```

## End-to-end flow

1. Start the main GraphQL app first.
2. Start this subscription client app.
3. Run a mutation in the main app such as `createEmployee`, `updateEmployeeById`, or `deleteEmployee`
4. The main app emits the employee event.
5. This client receives the event and logs the employee data.

## Example mutation to test from main app

### Create Employee

```graphql
mutation CreateEmployee($id: ID!, $name: String!, $email: String!, $department: String!) {
  createEmployee(id: $id, name: $name, email: $email, department: $department) {
    id
    name
    email
    department
  }
}
```

Variables:

```json
{
  "id": "10",
  "name": "Kumar",
  "email": "kumar@gmail.com",
  "department": "IT"
}
```

### Update Employee

```graphql
mutation UpdateEmployee($id: ID!, $name: String, $email: String, $department: String) {
  updateEmployeeById(id: $id, name: $name, email: $email, department: $department) {
    id
    name
    email
    department
  }
}
```

Variables:

```json
{
  "id": "10",
  "name": "Kumar S",
  "email": "kumars@gmail.com",
  "department": "Platform"
}
```

### Delete Employee

```graphql
mutation DeleteEmployee($id: ID!) {
  deleteEmployee(id: $id)
}
```

Variables:

```json
{
  "id": "10"
}
```

## Simple architecture

- Main app = **emits** employee events.
- Client app = **subscribes and consumes** employee events
- Communication happens using GraphQL **subscription** over WebSocket.
