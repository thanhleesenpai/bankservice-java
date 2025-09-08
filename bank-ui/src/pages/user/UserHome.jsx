import React from "react";
import { Container, Typography, Box, Grid, Paper } from "@mui/material";
import UserAppBar from "./UserAppBar";

export default function UserHome() {
  return (
    <div>
      <UserAppBar />
      <Container sx={{ mt: 4 }}>
        <Typography variant="h4" gutterBottom>
          Chào mừng bạn đến với MyBank!
        </Typography>
        <Typography variant="body1" gutterBottom>
          Đây là trang chủ tài khoản khách hàng. Bạn có thể xem thông tin tài khoản,
          quản lý thẻ, thực hiện giao dịch và nhiều dịch vụ khác.
        </Typography>

        {/* Các ô chức năng */}
        <Box sx={{ mt: 4 }}>
          <Grid container spacing={3}>
            <Grid item xs={12} sm={6} md={3}>
              <Paper sx={{ p: 2, textAlign: "center" }} elevation={3}>
                <Typography variant="h6">Số dư</Typography>
                <Typography variant="body1">Xem và quản lý số dư tài khoản</Typography>
              </Paper>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Paper sx={{ p: 2, textAlign: "center" }} elevation={3}>
                <Typography variant="h6">Quản lý thẻ</Typography>
                <Typography variant="body1">Thêm, xóa hoặc khóa thẻ</Typography>
              </Paper>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Paper sx={{ p: 2, textAlign: "center" }} elevation={3}>
                <Typography variant="h6">Chuyển tiền</Typography>
                <Typography variant="body1">Thực hiện giao dịch chuyển tiền</Typography>
              </Paper>
            </Grid>

            <Grid item xs={12} sm={6} md={3}>
              <Paper sx={{ p: 2, textAlign: "center" }} elevation={3}>
                <Typography variant="h6">Lịch sử</Typography>
                <Typography variant="body1">Xem lịch sử giao dịch</Typography>
              </Paper>
            </Grid>
          </Grid>
        </Box>
      </Container>
    </div>
  );
}
