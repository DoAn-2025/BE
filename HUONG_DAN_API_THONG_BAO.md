# Hướng dẫn sử dụng API Thông báo (Notification API)

## Tổng quan
Tài liệu này hướng dẫn Frontend team cách sử dụng các API thông báo trong hệ thống Web TOEIC.

## URL Gốc
```
/api/v1/noti
```

## Xác thực
Tất cả các endpoint đều yêu cầu JWT token trong header Authorization:
```
Authorization: Bearer <your_jwt_token>
```

---

## Các API Endpoints

### 1. Đếm số thông báo chưa đọc

**Endpoint:** `GET /api/v1/noti/count`

**Mục đích:** Lấy tổng số thông báo chưa đọc của user hiện tại (dùng để hiển thị badge)

**Ví dụ Response:**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": 5
}
```

**Cách sử dụng trong React:**
```javascript
const [unreadCount, setUnreadCount] = useState(0);

// Lấy số lượng thông báo chưa đọc
const fetchUnreadCount = async () => {
  const response = await fetch('/api/v1/noti/count', {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  });
  const data = await response.json();
  setUnreadCount(data.data);
};

// Gọi mỗi 30 giây để cập nhật
useEffect(() => {
  fetchUnreadCount();
  const interval = setInterval(fetchUnreadCount, 30000);
  return () => clearInterval(interval);
}, []);
```

---

### 2. Lấy danh sách thông báo (có phân trang)

**Endpoint:** `GET /api/v1/noti/list`

**Query Parameters:**
- `page`: Số trang (bắt đầu từ 0)
- `size`: Số lượng item mỗi trang (mặc định 20)
- `sort`: Sắp xếp (ví dụ: "createdAt,desc")

**Ví dụ:** `/api/v1/noti/list?page=0&size=10&sort=createdAt,desc`

**Response:**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Khóa học mới",
        "content": "Khóa học TOEIC Listening mới đã được thêm vào hệ thống",
        "objectId": 15,
        "notiType": "NEW_COURSE",
        "createdAt": "2025-12-09T10:30:00.000Z",
        "isRead": false
      }
    ],
    "totalPages": 2,
    "totalElements": 15,
    "size": 10,
    "number": 0,
    "first": true,
    "last": false
  }
}
```

**Các loại thông báo (notiType):**
- `NEW_COURSE`: Khóa học mới (cho tất cả học viên)
- `ADD_TO_CLASS`: Được thêm vào lớp (cho học viên và giáo viên)
- `NEW_QUIZ_IN_CLASS`: Bài quiz mới trong lớp (cho học viên và giáo viên)
- `UPDATE_IN_CLASS`: Thông báo/cập nhật trong lớp (cho học viên và giáo viên)

**Cách sử dụng trong React:**
```javascript
const [notifications, setNotifications] = useState([]);
const [page, setPage] = useState(0);
const [totalPages, setTotalPages] = useState(0);

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

useEffect(() => {
  fetchNotifications();
}, [page]);
```

---

### 3. Đánh dấu thông báo đã đọc

**Endpoint:** `POST /api/v1/noti/update`

**Request Body:** Mảng các ID thông báo cần đánh dấu đã đọc
```json
[1, 2, 3, 5]
```

**Response:**
```json
{
  "code": 200,
  "message": "Get Notification is successfully",
  "data": null
}
```

**Cách sử dụng trong React:**

**Đánh dấu 1 thông báo đã đọc:**
```javascript
const markAsRead = async (notificationId) => {
  await fetch('/api/v1/noti/update', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify([notificationId])
  });
  
  // Refresh lại danh sách
  fetchNotifications();
  fetchUnreadCount();
};
```

**Đánh dấu tất cả đã đọc:**
```javascript
const markAllAsRead = async () => {
  const unreadIds = notifications
    .filter(n => !n.isRead)
    .map(n => n.id);
  
  if (unreadIds.length > 0) {
    await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(unreadIds)
    });
    
    // Refresh lại
    fetchNotifications();
    fetchUnreadCount();
  }
};
```

---

## Component Mẫu - Notification Dropdown

```javascript
import React, { useState, useEffect } from 'react';

function NotificationDropdown() {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isOpen, setIsOpen] = useState(false);
  const token = localStorage.getItem('token');

  // Lấy số lượng chưa đọc
  const fetchUnreadCount = async () => {
    const response = await fetch('/api/v1/noti/count', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const data = await response.json();
    setUnreadCount(data.data);
  };

  // Lấy 5 thông báo mới nhất
  const fetchNotifications = async () => {
    const response = await fetch(
      '/api/v1/noti/list?page=0&size=5&sort=createdAt,desc',
      {
        headers: { 'Authorization': `Bearer ${token}` }
      }
    );
    const data = await response.json();
    setNotifications(data.data.content);
  };

  // Đánh dấu đã đọc
  const handleNotificationClick = async (id) => {
    await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([id])
    });
    
    fetchNotifications();
    fetchUnreadCount();
  };

  useEffect(() => {
    fetchUnreadCount();
    fetchNotifications();
    
    // Auto refresh mỗi 30 giây
    const interval = setInterval(() => {
      fetchUnreadCount();
      fetchNotifications();
    }, 30000);
    
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="notification-dropdown">
      <button onClick={() => setIsOpen(!isOpen)}>
        🔔
        {unreadCount > 0 && (
          <span className="badge">{unreadCount}</span>
        )}
      </button>
      
      {isOpen && (
        <div className="dropdown-menu">
          <h3>Thông báo</h3>
          {notifications.map(noti => (
            <div
              key={noti.id}
              className={`notification-item ${noti.isRead ? 'read' : 'unread'}`}
              onClick={() => handleNotificationClick(noti.id)}
            >
              <h4>{noti.title}</h4>
              <p>{noti.content}</p>
              <small>
                {new Date(noti.createdAt).toLocaleString('vi-VN')}
              </small>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default NotificationDropdown;
```

---

## Component Mẫu - Trang Thông báo đầy đủ

```javascript
import React, { useState, useEffect } from 'react';

function NotificationPage() {
  const [notifications, setNotifications] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [unreadCount, setUnreadCount] = useState(0);
  const token = localStorage.getItem('token');

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

  const fetchUnreadCount = async () => {
    const response = await fetch('/api/v1/noti/count', {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const data = await response.json();
    setUnreadCount(data.data);
  };

  const markAsRead = async (id) => {
    await fetch('/api/v1/noti/update', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify([id])
    });
    
    fetchNotifications();
    fetchUnreadCount();
  };

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
        <h1>Thông báo ({unreadCount} chưa đọc)</h1>
        <button onClick={markAllAsRead}>
          Đánh dấu tất cả đã đọc
        </button>
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
            <div className="meta">
              <small>
                {new Date(noti.createdAt).toLocaleString('vi-VN')}
              </small>
              <span className="badge">{noti.notiType}</span>
            </div>
          </div>
        ))}
      </div>
      
      {/* Phân trang */}
      <div className="pagination">
        <button
          onClick={() => setPage(p => Math.max(0, p - 1))}
          disabled={page === 0}
        >
          Trang trước
        </button>
        <span>Trang {page + 1} / {totalPages}</span>
        <button
          onClick={() => setPage(p => p + 1)}
          disabled={page >= totalPages - 1}
        >
          Trang sau
        </button>
      </div>
    </div>
  );
}

export default NotificationPage;
```

---

## Lưu ý quan trọng

1. **Token Authentication:**
   - Luôn gửi JWT token trong header `Authorization`
   - Format: `Bearer <token>`
   - Lưu token trong localStorage hoặc httpOnly cookie

2. **Xử lý lỗi:**
   - Code 401: Token hết hạn hoặc không hợp lệ → Redirect về trang login
   - Code 404: Không tìm thấy user hoặc thông báo
   - Code 200: Thành công

3. **Performance:**
   - Polling mỗi 30-60 giây để cập nhật số lượng thông báo
   - Không poll quá thường xuyên để tránh tải server
   - Sử dụng pagination khi hiển thị danh sách

4. **UX/UI:**
   - Hiển thị badge với số lượng thông báo chưa đọc
   - Khác biệt giữa thông báo đã đọc và chưa đọc (màu sắc, font)
   - Tự động đánh dấu đã đọc khi user click vào thông báo

5. **Timestamp:**
   - Tất cả timestamp đều ở format ISO 8601 (UTC)
   - Sử dụng `new Date(noti.createdAt).toLocaleString('vi-VN')` để hiển thị

---

## Swagger UI

Để xem tài liệu API tương tác, truy cập:
```
http://localhost:8888/swagger-ui/index.html
```

Tại đây bạn có thể:
- Xem chi tiết tất cả các endpoint
- Test API trực tiếp từ browser
- Xem request/response examples

---

## Liên hệ

Nếu có vấn đề hoặc câu hỏi về API, vui lòng liên hệ team Backend.

**Version:** 1.0  
**Cập nhật lần cuối:** 09/12/2025
