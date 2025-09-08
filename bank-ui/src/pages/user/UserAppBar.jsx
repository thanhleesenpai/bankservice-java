import React, { useEffect, useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Badge,
  Menu,
  MenuItem,
} from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import { Link, useNavigate } from "react-router-dom";
import SockJS from "sockjs-client";
// import { over } from "stompjs";
// import Stomp from "stompjs";
import { Client } from '@stomp/stompjs';
import { jwtDecode } from "jwt-decode";

export default function UserAppBar() {
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);
  const [anchorEl, setAnchorEl] = useState(null);
  const [stompClient, setStompClient] = useState(null);
  const [userId, setUserId] = useState(null);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        setUserId(decodedToken.sub);
      } catch (e) {
        console.error("Invalid token:", e);
        handleLogout();
      }
    }
  }, []);

  useEffect(() => {
    if (userId) {
      const token = localStorage.getItem("token");
      fetch(`/api/notifications/received/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((response) => response.json())
        .then((receivedNotis) => {
          fetch(`/api/notifications/sent/${userId}`, {
            headers: { Authorization: `Bearer ${token}` },
          })
            .then((response) => response.json())
            .then((sentNotis) => {
              const allNotis = [...receivedNotis, ...sentNotis];
              allNotis.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
              setNotifications(allNotis);
            });
        })
        .catch((error) => {
          console.error("Error fetching notifications:", error);
        });
    }
  }, [userId]);

  // useEffect(() => {
  //   const socket = new SockJS("/ws");
  //   //const stomp = over(socket);
  //   // const stomp = Stomp.client(socket);
  //   const stomp = new Client({ webSocketFactory: () => socket });
  //   stomp.connect({}, () => {
  //     if (userId) {
  //       stomp.subscribe("/topic/notifications", (message) => {
  //         const newNoti = JSON.parse(message.body);
  //         if (newNoti.toAccountId === userId || newNoti.fromAccountId === userId) {
  //           setNotifications((prev) => [newNoti, ...prev]);
  //           setUnreadCount((prev) => prev + 1);
  //         }
  //       });
  //     }
  //   });
  //   setStompClient(stomp);

  //   return () => {
  //     if (stompClient) stompClient.disconnect();
  //   };
  // }, [userId]);
  useEffect(() => {
    if (!userId) return;

    const socket = new SockJS("/ws");
    const stomp = new Client({ webSocketFactory: () => socket });

    stomp.onConnect = () => {
        console.log('Connected to STOMP');
        stomp.subscribe("/topic/notifications", (message) => {
            const newNoti = JSON.parse(message.body);
            if (newNoti.toAccountId === userId || newNoti.fromAccountId === userId) {
                setNotifications((prev) => [newNoti, ...prev]);
                setUnreadCount((prev) => prev + 1);
            }
        });
    };

    stomp.activate();

    setStompClient(stomp);

    return () => {
        if (stompClient) {
            stompClient.deactivate();
        }
    };
  }, [userId]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    navigate("/login");
  };

  const handleOpenMenu = (event) => {
    setAnchorEl(event.currentTarget);
    setUnreadCount(0);
  };

  const handleCloseMenu = () => setAnchorEl(null);

  return (
    <AppBar position="static" color="primary">
      <Toolbar>
        {/* Logo */}
        <Typography
          variant="h6"
          component={Link}
          to="/user-home"
          style={{ textDecoration: "none", color: "white", fontWeight: "bold" }}
        >
          MyBank
        </Typography>
  
        {/* Menu */}
        <Box sx={{ flexGrow: 1, display: "flex", gap: 2, ml: 4 }}>
          <Button color="inherit" component={Link} to="/user-home">
            Trang Chủ
          </Button>
          <Button color="inherit" component={Link} to="/account">
            Tài Khoản
          </Button>
          {/* THÊM MỚI: Nút Lịch sử */}
          <Button color="inherit" component={Link} to="/transaction-history">
            Lịch sử
          </Button>
          <Button color="inherit">Dịch Vụ</Button>
          <Button color="inherit">Liên Hệ</Button>
        </Box>
  
        {/* Icon thông báo */}
        <IconButton color="inherit" onClick={handleOpenMenu}>
          <Badge badgeContent={unreadCount} color="error">
            <NotificationsIcon />
          </Badge>
        </IconButton>
  
        <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleCloseMenu}>
          {notifications.length === 0 ? (
            <MenuItem>Không có thông báo</MenuItem>
          ) : (
            notifications.map((noti, idx) => (
              <MenuItem
                key={noti.paymentId || idx}
                // THAY ĐỔI: Thêm hàm onClick để chuyển hướng
                onClick={() => {
                  handleCloseMenu();
                  navigate("/transaction-history");
                }}
                sx={{
                  flexDirection: 'column',
                  alignItems: 'flex-start',
                  borderBottom: '1px solid #eee',
                  padding: '10px 16px'
                }}
              >
                {/* Hiển thị thông báo chi tiết */}
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  {noti.fromAccountId === userId
                    ? `Bạn đã chuyển tiền`
                    : `Bạn đã nhận được tiền`}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Số tiền: {noti.amount} VNĐ
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Từ tài khoản: {noti.fromAccountId}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  Đến tài khoản: {noti.toAccountId}
                </Typography>
              </MenuItem>
            ))
          )}
        </Menu>
  
        {/* Logout */}
        <Button variant="contained" color="secondary" onClick={handleLogout}>
          Đăng Xuất
        </Button>
      </Toolbar>
    </AppBar>
  );
}