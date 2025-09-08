// src/components/User/Account.jsx
import React, { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import UserAppBar from "./UserAppBar";
import { useNavigate } from "react-router-dom";

export default function Account() {
  const [account, setAccount] = useState(null);
  const [cards, setCards] = useState([]);
  const [balances, setBalances] = useState([]);
  const token = localStorage.getItem("token");
  const [newCard, setNewCard] = useState({
    cardType: "",
    expiryDate: "",
    status: "active",
  });
  const [showForm, setShowForm] = useState(false);
  const [transaction, setTransaction] = useState({ amount: "" });
  //const [activeDepositCard, setActiveDepositCard] = useState(null);
  const [activeWithdrawCard, setActiveWithdrawCard] = useState(null);
  const navigate = useNavigate();


  const handleTransfer = (cardId) => {
    navigate(`/transfer/${cardId}`); // điều hướng đến trang chuyển khoản với cardId
  };

  // Hàm lấy accountId từ token
  const getAccountIdFromToken = () => {
    if (!token) return null;
    try {
      const decoded = jwtDecode(token);
      console.log("Decoded token:", decoded);
      console.log("Account ID:", decoded.sub);
      return decoded.sub;
    } catch (error) {
      console.error("Invalid token:", error);
      return null;
    }
  };

  useEffect(() => {
    const accountId = getAccountIdFromToken();
    console.log("Token:", token);
    if (!accountId) return;
    console.log("Account ID from token:", accountId);

    // Lấy thông tin tài khoản
    fetch(`/bankservice/api/accounts/${accountId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        
        // eslint-disable-next-line no-unused-vars
        const { password, ...safeData } = data;
        console.log("Account data:", safeData);
        setAccount(safeData);
      })
      .catch((err) => console.error("Error fetching account:", err));

    // Lấy danh sách thẻ
    fetch(`/bankservice/api/cards/my`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => setCards(data))
      .catch((err) => console.error("Error fetching cards:", err));
  }, []);
  const fetchBalances = () => {
    fetch("/bankservice/api/balances", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => setBalances(data))
      .catch((err) => console.error(err));
  };
  
  useEffect(() => {
    fetchBalances();
  }, [token]);
  

  if (!account) return <p>Đang tải...</p>;
  const createCard = () => {
    if (!newCard.cardType.trim()) {
      alert("Vui lòng chọn loại thẻ!");
      return;
    }
    fetch("/bankservice/api/cards", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(newCard),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("Card created:", data);
        setCards((prev) => [...prev, data]);
        setShowForm(false);
        setNewCard({ cardType: "", expiryDate: "", status: "ACTIVE" });
      })
      .catch((err) => console.error("Error creating card:", err));
  };


  // const depositMoney = (accountId) => {
  //   if (!transaction.amount || isNaN(transaction.amount) || transaction.amount <= 0) {
  //     alert("Số tiền không hợp lệ!");
  //     return;
  //   }
  //   fetch(`/bankservice/api/balances/deposit/${accountId}`, {
  //     method: "PUT",
  //     headers: {
  //       "Content-Type": "application/json",
  //       Authorization: `Bearer ${token}`,
  //     },
  //     body: JSON.stringify({ amount: parseFloat(transaction.amount) }),
  //   })
  //     .then((res) => res.json())
  //     .then((data) => {
  //       console.log("Deposit result:", data);
  //       setBalances(data);
  //       alert("Nạp tiền thành công!");
  //       setTransaction({ amount: "" });
  //       setShowDepositForm(false);
  //       fetchBalances();
  //     })
  //     .catch((err) => console.error("Error depositing:", err));
  // };
  
  const withdrawMoney = (accountId) => {
    if (!transaction.amount || isNaN(transaction.amount) || transaction.amount <= 0) {
      alert("Số tiền không hợp lệ!");
      return;
    }
    fetch(`/bankservice/transactions/withdraw/${accountId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ amount: parseFloat(transaction.amount) }),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("Withdraw result:", data);
        setBalances(data);
        alert("Rút tiền thành công!");
        setTransaction({ amount: "" });
        // setShowWithdrawForm(false);
        fetchBalances();
      })
      .catch((err) => console.error("Error withdrawing:", err));
  };

  return (
    <div>
      <UserAppBar />
      <div style={{ padding: "20px" }}>
        <h2>Thông tin Tài khoản và Chuyển khoản</h2>

        <div style={{ border: "1px solid #ccc", padding: "15px", borderRadius: "8px" }}>
          <h3>Thông tin Tài khoản</h3>
          <p><strong>Họ và tên:</strong> {account.customerName}</p>
          <p><strong>Email:</strong> {account.email}</p>
          <p><strong>Số điện thoại:</strong> {account.phoneNumber}</p>
          <p><strong>Vai trò:</strong> {account.role}</p>
        </div>

        <div style={{ marginTop: "20px" }}>
          <h3>Danh sách thẻ của bạn</h3>

          <button style={{ marginBottom: "10px" }} onClick={() => setShowForm(!showForm)}>
            Create card
          </button>

          {showForm && (
            <div style={{ marginBottom: "15px", padding: "10px", border: "1px solid #ccc" }}>
              <label>
                Loại thẻ:{" "}
                <input
                  type="text"
                  value={newCard.cardType}
                  onChange={(e) => setNewCard({ ...newCard, cardType: e.target.value })}
                  placeholder="VISA / DEBIT / CREDIT"
                />
              </label>
              <br />
              <label>
                Ngày hết hạn:{" "}
                <input
                  type="date"
                  value={newCard.expiryDate}
                  onChange={(e) => setNewCard({ ...newCard, expiryDate: e.target.value })}
                />
              </label>
              <br />
              <label>
                Trạng thái:{" "}
                <select
                  value={newCard.status}
                  onChange={(e) => setNewCard({ ...newCard, status: e.target.value })}
                >
                  <option value="active">active</option>
                  <option value="inactive">inactive</option>
                </select>
              </label>
              <br />
              <button onClick={createCard} style={{ marginTop: "5px" }}>
                Xác nhận tạo thẻ
              </button>
            </div>
          )}

          {cards.length > 0 ? (
            cards.map((card) => (
              <div key={card.cardId} style={{ border: "1px solid #ddd", padding: "10px", marginBottom: "8px" }}>
    <p>
      Số thẻ: {card.cardId} - Loại thẻ: {card.cardType} - Ngày hết hạn: {card.expiryDate} - 
      Số dư: {balances.availableBalance} - Đang chờ xử lý: {balances.holdBalance}
    </p>

    {/* Nạp tiền */}
    {/* <button onClick={() => setActiveDepositCard(activeDepositCard === card.cardId ? null : card.cardId)}>
      Nạp tiền
    </button> */}
    <button 
      onClick={() => handleTransfer(card.cardId)}
      style={{ marginLeft: "10px", backgroundColor: "orange", color: "white" }}
    >
      Chuyển khoản
    </button>


    {/* Rút tiền */}
    <button 
      onClick={() => setActiveWithdrawCard(activeWithdrawCard === card.cardId ? null : card.cardId)}
      style={{ marginLeft: "10px" }}
    >
      Rút tiền
    </button>

    {/* Form Nạp tiền */}
    {/* {activeDepositCard === card.cardId && (
      <div>
        <input
          type="number"
          placeholder="Nhập số tiền"
          value={transaction.amount}
          onChange={(e) => setTransaction({ amount: e.target.value })}
        />
        <button onClick={() => depositMoney(account.accountId)}>Xác nhận nạp</button>
      </div>
    )} */}

    {/* Form Rút tiền */}
    {activeWithdrawCard === card.cardId && (
      <div>
        <input
          type="number"
          placeholder="Nhập số tiền"
          value={transaction.amount}
          onChange={(e) => setTransaction({ amount: e.target.value })}
        />
        <button onClick={() => withdrawMoney(account.accountId)}>Xác nhận rút</button>
      </div>
    )}
  </div>
            ))
            ) : (
              <p>Chưa có thẻ nào</p>
            )
          }
        </div>
      </div>
    </div>
  );
}
