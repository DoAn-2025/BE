# Notification API Documentation

## Overview
This document provides comprehensive information about the Notification APIs available in the Web TOEIC backend system. These APIs allow frontend applications to retrieve and manage user notifications.

## Base URL
```
/api/v1/noti
```

## Authentication
All notification endpoints require authentication. Include a valid JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## API Endpoints

### 1. Count Unread Notifications

Get the total number of unread notifications for the current user.

#### Endpoint
```http
GET /api/v1/noti/count
```

#### Headers
| Header | Type | Required | Description |
|--------|------|----------|-------------|
| Authorization | string | Yes | Bearer token for authentication |

#### Response

**Success Response (200 OK)**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": 5
}
```

**Response Fields**
| Field | Type | Description |
|-------|------|-------------|
| code | integer | HTTP status code |
| message | string | Response message |
| data | long | Number of unread notifications |

**Error Responses**

- **401 Unauthorized**
```json
{
  "code": 401,
  "message": "Unauthenticated"
}
```

- **404 Not Found**
```json
{
  "code": 404,
  "message": "User not existed"
}
```

#### Example Usage

**cURL**
```bash
curl -X GET "http://localhost:8080/api/v1/noti/count" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**JavaScript (Fetch API)**
```javascript
fetch('/api/v1/noti/count', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
.then(response => response.json())
.then(data => console.log('Unread notifications:', data.data));
```

**Axios**
```javascript
axios.get('/api/v1/noti/count', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(response => {
  console.log('Unread count:', response.data.data);
});
```

---

### 2. Get Notification List

Retrieve a paginated list of notifications for the current user.

#### Endpoint
```http
GET /api/v1/noti/list
```

#### Headers
| Header | Type | Required | Description |
|--------|------|----------|-------------|
| Authorization | string | Yes | Bearer token for authentication |

#### Query Parameters
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | integer | No | 0 | Page number (0-indexed) |
| size | integer | No | 20 | Number of items per page |
| sort | string | No | - | Sort criteria (e.g., "createdAt,desc") |

#### Response

**Success Response (200 OK)**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "New Course Available",
        "content": "A new TOEIC Listening course has been added to the platform",
        "objectId": 15,
        "notiType": "NEW_COURSE",
        "createdAt": "2025-12-09T10:30:00.000Z",
        "isRead": false
      },
      {
        "id": 2,
        "title": "Added to Class",
        "content": "You have been added to TOEIC Advanced Class",
        "objectId": 8,
        "notiType": "ADD_TO_CLASS",
        "createdAt": "2025-12-08T14:20:00.000Z",
        "isRead": true
      },
      {
        "id": 3,
        "title": "New Quiz Available",
        "content": "A new quiz has been posted in your class",
        "objectId": 23,
        "notiType": "NEW_QUIZ_IN_CLASS",
        "createdAt": "2025-12-07T09:15:00.000Z",
        "isRead": false
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 2,
    "totalElements": 15,
    "last": false,
    "size": 10,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 10,
    "first": true,
    "empty": false
  }
}
```

**Response Fields**

**Notification Object**
| Field | Type | Description |
|-------|------|-------------|
| id | long | Unique notification identifier |
| title | string | Notification title |
| content | string | Notification message/content |
| objectId | long | Related object ID (course, class, quiz, etc.) |
| notiType | string | Type of notification (see Notification Types below) |
| createdAt | string | ISO 8601 timestamp of notification creation |
| isRead | boolean | Whether the notification has been read |

**Pagination Fields**
| Field | Type | Description |
|-------|------|-------------|
| content | array | Array of notification objects |
| totalPages | integer | Total number of pages |
| totalElements | integer | Total number of notifications |
| size | integer | Number of items per page |
| number | integer | Current page number (0-indexed) |
| first | boolean | Whether this is the first page |
| last | boolean | Whether this is the last page |
| empty | boolean | Whether the result is empty |

**Notification Types**
| Type | Description |
|------|-------------|
| NEW_COURSE | New course added to platform (for all students) |
| ADD_TO_CLASS | User added to a class (for students and teachers) |
| NEW_QUIZ_IN_CLASS | New quiz posted in class (for students and teachers) |
| UPDATE_IN_CLASS | Update/announcement in class (for students and teachers) |

**Error Responses**

- **401 Unauthorized**
```json
{
  "code": 401,
  "message": "Unauthenticated"
}
```

- **404 Not Found**
```json
{
  "code": 404,
  "message": "User not existed"
}
```

#### Example Usage

**cURL**
```bash
curl -X GET "http://localhost:8080/api/v1/noti/list?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**JavaScript (Fetch API)**
```javascript
const params = new URLSearchParams({
  page: 0,
  size: 10,
  sort: 'createdAt,desc'
});

fetch(`/api/v1/noti/list?${params}`, {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer ' + token
  }
})
.then(response => response.json())
.then(data => {
  console.log('Notifications:', data.data.content);
  console.log('Total pages:', data.data.totalPages);
});
```

**Axios**
```javascript
axios.get('/api/v1/noti/list', {
  params: {
    page: 0,
    size: 10,
    sort: 'createdAt,desc'
  },
  headers: {
    'Authorization': `Bearer ${token}`
  }
})
.then(response => {
  const notifications = response.data.data.content;
  console.log('Notifications:', notifications);
});
```

**React Example**
```javascript
import { useState, useEffect } from 'react';

function NotificationList() {
  const [notifications, setNotifications] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchNotifications = async () => {
      const response = await fetch(
        `/api/v1/noti/list?page=${page}&size=10&sort=createdAt,desc`,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );
      const data = await response.json();
      setNotifications(data.data.content);
      setTotalPages(data.data.totalPages);
    };

    fetchNotifications();
  }, [page]);

  return (
    <div>
      {notifications.map(noti => (
        <div key={noti.id} className={noti.isRead ? 'read' : 'unread'}>
          <h3>{noti.title}</h3>
          <p>{noti.content}</p>
          <small>{new Date(noti.createdAt).toLocaleString()}</small>
        </div>
      ))}
    </div>
  );
}
```

---

### 3. Mark Notifications as Read

Update the read status of one or more notifications.

#### Endpoint
```http
POST /api/v1/noti/update
```

#### Headers
| Header | Type | Required | Description |
|--------|------|----------|-------------|
| Authorization | string | Yes | Bearer token for authentication |
| Content-Type | string | Yes | application/json |

#### Request Body
Array of notification IDs to mark as read.

```json
[1, 2, 3, 5, 8]
```

**Body Parameters**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| (array) | array of long | Yes | List of notification IDs to mark as read |

#### Response

**Success Response (200 OK)**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": null
}
```

**Response Fields**
| Field | Type | Description |
|-------|------|-------------|
| code | integer | HTTP status code |
| message | string | Response message |
| data | null | No data returned for this operation |

**Error Responses**

- **401 Unauthorized**
```json
{
  "code": 401,
  "message": "Unauthenticated"
}
```

- **404 Not Found**
```json
{
  "code": 404,
  "message": "User not existed"
}
```
or
```json
{
  "code": 404,
  "message": "Notification not existed"
}
```

#### Example Usage

**cURL**
```bash
curl -X POST "http://localhost:8080/api/v1/noti/update" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3]'
```

**JavaScript (Fetch API)**
```javascript
const notificationIds = [1, 2, 3, 5];

fetch('/api/v1/noti/update', {
  method: 'POST',
  headers: {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(notificationIds)
})
.then(response => response.json())
.then(data => console.log('Notifications marked as read:', data));
```

**Axios**
```javascript
const notificationIds = [1, 2, 3];

axios.post('/api/v1/noti/update', notificationIds, {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => {
  console.log('Success:', response.data.message);
})
.catch(error => {
  console.error('Error:', error.response.data);
});
```

**React Example - Mark Single Notification as Read**
```javascript
const markAsRead = async (notificationId) => {
  try {
    const response = await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([notificationId])
    });
    
    if (response.ok) {
      console.log('Notification marked as read');
      // Refresh notification list
    }
  } catch (error) {
    console.error('Error marking notification as read:', error);
  }
};
```

**React Example - Mark All as Read**
```javascript
const markAllAsRead = async (notifications) => {
  const unreadIds = notifications
    .filter(n => !n.isRead)
    .map(n => n.id);
  
  if (unreadIds.length === 0) return;

  try {
    await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(unreadIds)
    });
    
    console.log('All notifications marked as read');
    // Refresh notification list
  } catch (error) {
    console.error('Error:', error);
  }
};
```

---

## Common Use Cases

### 1. Display Notification Badge
Show unread notification count in a badge:

```javascript
async function updateNotificationBadge() {
  const response = await fetch('/api/v1/noti/count', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const data = await response.json();
  
  // Update badge in UI
  document.getElementById('notification-badge').textContent = data.data;
}

// Call every 30 seconds
setInterval(updateNotificationBadge, 30000);
```

### 2. Notification Dropdown
Display recent notifications in a dropdown:

```javascript
async function loadNotificationDropdown() {
  const response = await fetch('/api/v1/noti/list?page=0&size=5&sort=createdAt,desc', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const data = await response.json();
  
  const notifications = data.data.content;
  // Render notifications in dropdown
}
```

### 3. Mark as Read on Click
Mark notification as read when user clicks on it:

```javascript
async function handleNotificationClick(notificationId) {
  // Mark as read
  await fetch('/api/v1/noti/update', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify([notificationId])
  });
  
  // Navigate to related object (course, class, quiz, etc.)
  // navigateToObject(notification.objectId, notification.notiType);
}
```

### 4. Full Notification Page with Pagination
Complete example with pagination:

```javascript
import React, { useState, useEffect } from 'react';

function NotificationPage() {
  const [notifications, setNotifications] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [unreadCount, setUnreadCount] = useState(0);
  const token = localStorage.getItem('token');

  // Fetch notifications
  const fetchNotifications = async () => {
    const response = await fetch(
      `/api/v1/noti/list?page=${page}&size=10&sort=createdAt,desc`,
      {
        headers: { 'Authorization': `Bearer ${token}` }
      }
    );
    const data = await response.json();
    setNotifications(data.data.content);
    setTotalPages(data.data.totalPages);
  };

  // Fetch unread count
  const fetchUnreadCount = async () => {
    const response = await fetch('/api/v1/noti/count', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const data = await response.json();
    setUnreadCount(data.data);
  };

  // Mark notification as read
  const markAsRead = async (id) => {
    await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([id])
    });
    
    // Refresh data
    fetchNotifications();
    fetchUnreadCount();
  };

  // Mark all as read
  const markAllAsRead = async () => {
    const unreadIds = notifications
      .filter(n => !n.isRead)
      .map(n => n.id);
    
    if (unreadIds.length > 0) {
      await fetch('/api/v1/noti/update', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(unreadIds)
      });
      
      fetchNotifications();
      fetchUnreadCount();
    }
  };

  useEffect(() => {
    fetchNotifications();
    fetchUnreadCount();
  }, [page]);

  return (
    <div className="notification-page">
      <div className="header">
        <h1>Notifications ({unreadCount} unread)</h1>
        <button onClick={markAllAsRead}>Mark All as Read</button>
      </div>
      
      <div className="notification-list">
        {notifications.map(noti => (
          <div 
            key={noti.id} 
            className={`notification-item ${noti.isRead ? 'read' : 'unread'}`}
            onClick={() => markAsRead(noti.id)}
          >
            <h3>{noti.title}</h3>
            <p>{noti.content}</p>
            <small>
              {new Date(noti.createdAt).toLocaleString()} - 
              Type: {noti.notiType}
            </small>
          </div>
        ))}
      </div>
      
      <div className="pagination">
        <button 
          onClick={() => setPage(p => Math.max(0, p - 1))}
          disabled={page === 0}
        >
          Previous
        </button>
        <span>Page {page + 1} of {totalPages}</span>
        <button 
          onClick={() => setPage(p => p + 1)}
          disabled={page >= totalPages - 1}
        >
          Next
        </button>
      </div>
    </div>
  );
}
```

---

## Error Handling

All API endpoints return consistent error responses. Here's how to handle them:

```javascript
async function apiCall(url, options) {
  try {
    const response = await fetch(url, options);
    const data = await response.json();
    
    if (data.code !== 200) {
      // Handle API error
      switch (data.code) {
        case 401:
          // Redirect to login
          window.location.href = '/login';
          break;
        case 404:
          console.error('Resource not found:', data.message);
          break;
        default:
          console.error('API Error:', data.message);
      }
      throw new Error(data.message);
    }
    
    return data;
  } catch (error) {
    console.error('Network error:', error);
    throw error;
  }
}
```

---

## Best Practices

1. **Polling for New Notifications**
   - Use a reasonable interval (30-60 seconds) to avoid server overload
   - Consider using WebSocket for real-time notifications in production

2. **Caching**
   - Cache notification count and list locally
   - Invalidate cache when marking notifications as read

3. **Pagination**
   - Use appropriate page sizes (10-20 items)
   - Implement infinite scroll or traditional pagination based on UX needs

4. **Error Handling**
   - Always handle authentication errors (401)
   - Provide user-friendly error messages
   - Implement retry logic for network failures

5. **Performance**
   - Batch mark-as-read operations when possible
   - Use optimistic UI updates for better UX

6. **Security**
   - Always include Authorization header
   - Store JWT token securely (httpOnly cookies preferred)
   - Handle token expiration gracefully

---

## Additional Notes

- All timestamps are in ISO 8601 format (UTC)
- The `objectId` field refers to the related entity (Course, Class, Quiz) based on `notiType`
- Notification content supports plain text and can contain HTML entities
- Users can only access and update their own notifications
- The system automatically marks notifications as unread when created

---

## Support

For issues or questions about the Notification API, please contact the development team or refer to the main API documentation.

**Version:** 1.0  
**Last Updated:** December 9, 2025
