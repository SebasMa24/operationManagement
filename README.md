# operationManagement

# Hardware Endpoints
GET    /api/operations/hardware
GET    /api/operations/hardware?filter={past|active|future|}
GET    /api/operations/hardware/user/{useremail}
GET    /api/operations/hardware/user/{useremail}?filter={past|active|future|}
GET    /api/operations/hardware/{reservationid}
GET    /api/operations/hardware/availability
POST   /api/operations/hardware
DELETE /api/operations/hardware/{reservationid}
POST   /api/operations/hardware/{reservationid}/handOver
POST   /api/operations/hardware/{reservationid}/return

# Space Endpoints
GET    /api/operations/space
GET    /api/operations/space?filter={past|active|future|}
GET    /api/operations/space/user/{useremail}
GET    /api/operations/space/user/{useremail}?filter={past|active|future|}
GET    /api/operations/space/{reservationid}
GET    /api/operations/space/availability
POST   /api/operations/space
DELETE /api/operations/space/{reservationid}
POST   /api/operations/space/{reservationid}/handOver
POST   /api/operations/space/{reservationid}/return


# Uso de Posts

Crear reserva de hardware
POST   /api/operations/hardware

Ejemplo body
{
  "building": 1,
  "resourceCode": 2,
  "storedResourceCode": 2,
  "requester": "usuario2@example.com",
  "manager": "usuario40@example.com",
  "start": "2025-06-15T10:00:00",
  "end": "2025-06-15T12:00:00"
}

Crear reserva de space
POST   /api/operations/space

Ejemplo body
{
  "building": 1,
  "resourceCode": 102,
  "requester": "usuario2@example.com",
  "manager": "usuario40@example.com",
  "start": "2025-06-15T10:00:00",
  "end": "2025-06-15T12:00:00"
}

Entrega de hardware o space 
POST   /api/operations/space/{reservationid}/handOver
POST   /api/operations/hardware/{reservationid}/handOver

No se necesita body

Devolución de hardware o space 
POST   /api/operations/hardware/{reservationid}/return
POST   /api/operations/space/{reservationid}/return

Ejemplo de body
{
  "conditionRate": 5,
  "serviceRate": 2
}