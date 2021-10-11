# Booking-Api


> this API intends to manage (create, modify, cancel and search ) bookings. 

## 💻 Requirements

* Java 8
* MySQL or docker-compose

## 🚀 Execution

To run the project on your local machine you need a instance of MySQL DB running.
if you have docker-compose configured on your machine you can just run the following command on project folder:

```
docker-compose up -d
```
If you don't have docker-compose, make sure that the credentials defined on application.properties match with your local MySQL instance

After guarantee that DB is running, just run the following command:
```
./gradlew bootRun
```

## ☕ Using Booking-Api

All endpoints use "YYYY-MM-DD" as date pattern. Below is the description of each one:

### search by date
```
GET /bookings/booking/{date}
```
just make a regular GET operation replacing the "{date}" with the desired date.

### search by a date range
```
GET /bookings?datein={date}&dateOut={date}
```
Also a regular GET but with dates passed by query params.

### create a booking
```
POST /bookings/booking
```
payload:
```json
{
  "dateIn": "2021-10-19",
  "dateOut": "2021-10-21",
  "clientId": 2
}
```
### modify a booking
```
PUT /bookings/booking
```
payload:
```json
{
  "id": 1,
  "dateIn": "2021-10-19",
  "dateOut": "2021-10-21",
  "clientId": 2
}
```
just pass the same payload of the creation with the booking ID e the updated dates.

### cancel a booking
```
DELETE /bookings/booking/{id}
```
a DELETE operation replacing the "{id}" with the booking ID.
