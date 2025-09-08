import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

export default function Transfer() {
  const { cardId } = useParams();
  const navigate = useNavigate();
  const [cardInfo, setCardInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [receiverCardNumber, setReceiverCardNumber] = useState('');
  const [receiverInfo, setReceiverInfo] = useState(null);
  const [amount, setAmount] = useState('');
  const [showOtpInput, setShowOtpInput] = useState(false);
  const [otp, setOtp] = useState('');
  const [transactionId, setTransactionId] = useState(null);
  const token = localStorage.getItem("token");

  useEffect(() => {
    fetch(`/bankservice/api/cards/${cardId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Lỗi khi lấy dữ liệu thẻ");
        }
        return response.json();
      })
      .then((data) => {
        setCardInfo(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error(error);
        setLoading(false);
      });
  }, [cardId, token]);

  if (loading) {
    return <p>Đang tải dữ liệu...</p>;
  }

  if (!cardInfo) {
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        <p>Không tìm thấy thông tin thẻ.</p>
        <button
          onClick={() => navigate(-1)}
          style={{ backgroundColor: "orange", marginTop: "10px" }}
        >
          Trở lại
        </button>
      </div>
    );
  }

  const handleSearch = () => {
    fetch(`/bankservice/api/cards/${receiverCardNumber}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Không tìm thấy người nhận");
        }
        return response.json();
      })
      .then((data) => {
        setReceiverInfo(data);
        alert(`Đã tìm thấy: ${data.customerName}`);
      })
      .catch((error) => {
        console.error(error);
        setReceiverInfo(null);
        alert(error.message);
      });
  };

  const handleConfirmTransfer = () => {
    if (!receiverInfo || !amount) {
      alert("Vui lòng nhập đủ thông tin người nhận và số tiền.");
      return;
    }

    const transactionData = {
      fromAccountId: cardInfo.accountId,
      toAccountId: receiverInfo.accountId,
      amount: parseFloat(amount),
    };

    fetch(`/bankservice/transactions/create`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(transactionData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Lỗi khi tạo giao dịch.");
        }
        return response.json();
      })
      .then((data) => {
        setTransactionId(data.id);
        setShowOtpInput(true);
        alert("Giao dịch đã được tạo. OTP đã được gửi tới email của bạn. Vui lòng nhập OTP để xác nhận.");
      })
      .catch((error) => {
        console.error(error);
        alert(error.message);
      });
  };

  // ... (các đoạn code khác giữ nguyên)

const handleVerifyOtp = () => {
  if (!otp) {
      alert("Vui lòng nhập mã OTP.");
      return;
  }

  const verificationData = {
      verificationCode: otp,
  };

  fetch(`/bankservice/transactions/${transactionId}/verify`, {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(verificationData),
  })
  .then((response) => {
      if (!response.ok) {
          // Trường hợp lỗi HTTP như 400 Bad Request
          throw new Error("Xác thực OTP thất bại. Vui lòng thử lại.");
      }
      return response.json();
  })
  .then((data) => {
      // Kiểm tra status từ dữ liệu trả về
      if (data.status === "AWAITING_APPROVAL") {
          alert("Chuyển khoản thành công!");
          // Reset form sau khi thành công
          setReceiverCardNumber('');
          setReceiverInfo(null);
          setAmount('');
          setShowOtpInput(false);
          setOtp('');
          setTransactionId(null);
      } else if (data.status === "FAILED") {
          alert("Bạn đã nhập sai OTP quá 3 lần. Giao dịch đã bị hủy.");
          // Reset form và các trạng thái liên quan
          setReceiverCardNumber('');
          setReceiverInfo(null);
          setAmount('');
          setShowOtpInput(false);
          setOtp('');
          setTransactionId(null);
      } else if (data.status === "PENDING") {
          // Khi OTP sai nhưng vẫn còn lượt thử
          alert(`Mã OTP không đúng. Bạn còn ${3 - data.attempts} lần thử.`);
          setOtp(''); // Xóa mã OTP đã nhập để người dùng nhập lại
      } else {
          alert("Trạng thái giao dịch không xác định.");
      }
  })
  .catch((error) => {
      console.error(error);
      alert(error.message);
  });
};

  if (loading) {
    return <p>Đang tải dữ liệu...</p>;
  }

  if (!cardInfo) {
    return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        <p>Không tìm thấy thông tin thẻ.</p>
        <button
          onClick={() => navigate(-1)}
          style={{ backgroundColor: "orange", marginTop: "10px" }}
        >
          Trở lại
        </button>
      </div>
    );
  }

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'flex-start',
      padding: '50px',
      gap: '20px'
    }}>
      {/* ... (giữ nguyên div Thông tin thẻ của bạn) */}
      <div style={{ border: "1px solid #ccc", padding: "15px", flex: 1, maxWidth: '400px' }}>
        <h3>Thông tin thẻ của bạn</h3>
        <p>Số thẻ: {cardInfo.cardId}</p>
        <p>Chủ thẻ: {cardInfo.customerName}</p>
        <p>Email: {cardInfo.email}</p>
        <p>Số điện thoại: {cardInfo.phoneNumber}</p>
        <p>Loại thẻ: {cardInfo.cardType}</p>
        <p>Ngày hết hạn: {cardInfo.expiryDate}</p>
        <p>Trạng thái: {cardInfo.status}</p>
        <p>Số dư khả dụng: {cardInfo.availableBalance.toLocaleString()}</p>
        <p>Số dư bị giữ: {cardInfo.holdBalance.toLocaleString()}</p>
        <button
          onClick={() => navigate(-1)}
          style={{ backgroundColor: "orange", marginTop: "10px" }}
        >
          Trở lại
        </button>
      </div>
      <div style={{ border: "1px solid #ccc", padding: "15px", flex: 1, maxWidth: '400px' }}>
        <h3>Thông tin người nhận</h3>
        <label>Số thẻ: </label>
        <input
          type="text"
          placeholder="Nhập số thẻ"
          value={receiverCardNumber}
          onChange={(e) => setReceiverCardNumber(e.target.value)}
        />
        <br />
        <button onClick={handleSearch} style={{ marginTop: "10px" }}>Tìm kiếm</button>
        {receiverInfo && (
          <div>
            <p>Số thẻ người nhận: {receiverInfo.cardId}</p>
            <p>Tên người nhận: {receiverInfo.customerName}</p>
            <label>Số tiền: </label>
            <input
              type="number"
              placeholder="Nhập số tiền"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />
            <button onClick={handleConfirmTransfer} style={{ marginTop: "10px", backgroundColor: "blue", color: "white" }}>Xác nhận chuyển khoản</button>
          </div>
        )}
        {showOtpInput && (
          <div style={{ marginTop: "20px" }}>
            <label>Mã OTP: </label>
            <input
              type="text"
              placeholder="Nhập mã OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
            />
            <button onClick={handleVerifyOtp} style={{ marginTop: "10px", backgroundColor: "green", color: "white" }}>Xác nhận OTP</button>
          </div>
        )}
      </div>
    </div>
  );
}