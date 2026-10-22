import React, { useEffect, useState } from "react";
import api from "./services/api";

const emptyProduct = {
  productName: "",
  category: "Vegetables",
  description: "",
  price: "",
  quantity: "",
  unit: "kg",
  imageUrl: "",
  farmer: { userId: "" },
};
function App() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState(emptyProduct);
  const [message, setMessage] = useState("");
  const [account, setAccount] = useState({
    fullName: "",
    email: "",
    password: "",
    phone: "",
    address: "",
    role: "FARMER",
  });
  const [login, setLogin] = useState({ email: "", password: "" });
  const [user, setUser] = useState(null);
  const [view, setView] = useState("market");
  const [dashboard, setDashboard] = useState(null);
  async function loadProducts() {
    try {
      const res = await api.get("/products", {
        params: search ? { search } : {},
      });
      setProducts(res.data);
    } catch (e) {
      setMessage(
        "Could not load products. Check that Spring Boot and MySQL are running.",
      );
    }
  }
  useEffect(() => {
    loadProducts();
  }, []);
  async function submitProduct(e) {
    e.preventDefault();
    try {
      const farmerId = form.farmer.userId || (user?.role === "FARMER" ? user.userId : null);
      if (!farmerId) {
        setMessage("Please specify your Farmer user ID.");
        return;
      }
      const payload = {
        ...form,
        price: Number(form.price),
        quantity: Number(form.quantity),
        farmer: { userId: Number(farmerId) },
      };
      await api.post("/products", payload);
      setMessage("Product added successfully!");
      setForm(emptyProduct);
      loadProducts();
      if (user?.role === "FARMER") {
        loadDashboard(user);
      }
    } catch (e) {
      setMessage(
        e.response?.data?.message ||
          "Product could not be saved. Confirm the farmer ID belongs to a registered FARMER.",
      );
    }
  }
  async function register(e) {
    e.preventDefault();
    try {
      const res = await api.post("/auth/register", account);
      setUser(res.data);
      setMessage("Account created successfully.");
      setView("dashboard");
      loadDashboard(res.data);
    } catch (e) {
      setMessage(
        e.response?.data?.message ||
          e.response?.data?.error ||
          "Registration failed. Please check your inputs.",
      );
    }
  }
  async function signIn(e) {
    e.preventDefault();
    try {
      const res = await api.post("/auth/login", login);
      setUser(res.data);
      setMessage("Logged in successfully.");
      setView("dashboard");
      loadDashboard(res.data);
    } catch (e) {
      setMessage(
        e.response?.data?.message ||
          "Login failed. Check your email and password.",
      );
    }
  }
  async function loadDashboard(who = user) {
    if (!who) return;
    try {
      const res = await api.get(
        `/dashboard/${who.role.toLowerCase()}/${who.userId}`,
      );
      setDashboard(res.data);
      setView("dashboard");
    } catch (e) {
      setMessage("Could not load dashboard.");
    }
  }
  async function removeProduct(id) {
    if (!window.confirm("Delete this product?")) return;
    try {
      await api.delete(`/products/${id}`);
      setMessage("Product deleted.");
      loadProducts();
    } catch (e) {
      setMessage("Could not delete product.");
    }
  }
  async function buy(product) {
    if (!user || user.role !== "CUSTOMER") {
      setMessage("Register or log in as a CUSTOMER to place an order.");
      setView("login");
      return;
    }
    const quantity = window.prompt(
      `How many ${product.unit || "units"} of ${product.productName}?`,
      "1",
    );
    if (!quantity) return;
    try {
      await api.post("/orders", {
        customerId: user.userId,
        items: [{ productId: product.productId, quantity: Number(quantity) }],
      });
      setMessage("Order placed successfully.");
      loadProducts();
      loadDashboard(user);
    } catch (e) {
      setMessage(e.response?.data?.message || "Order could not be placed.");
    }
  }
  return (
    <div className="app">
      <header className="topbar">
        <div className="brand">🌱 AgriMarket</div>
        <nav>
          <button onClick={() => setView("market")}>Marketplace</button>
          <button onClick={() => setView("register")}>Register</button>
          <button onClick={() => setView("login")}>Login</button>
          {user && (
            <button onClick={() => loadDashboard(user)}>Dashboard</button>
          )}
        </nav>
        <span className="signed">
          {user ? `${user.fullName} (${user.role})` : "Farm to home"}
        </span>
      </header>
      <section className="hero">
        <p className="eyebrow">GROW LOCAL · BUY FRESH</p>
        <h1>
          Fresh from farms,
          <br />
          straight to you.
        </h1>
        <p>Discover farm-grown produce from local farmers.</p>
        <div className="search">
          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search crops and products..."
          />
          <button onClick={loadProducts}>Search</button>
        </div>
      </section>
      {message && (
        <div className="message">
          <span>{message}</span>
          <button onClick={() => setMessage("")}>×</button>
        </div>
      )}
      <main>
        {view === "market" && (
          <>
            <div className="section-head">
              <div>
                <p className="eyebrow">MARKETPLACE</p>
                <h2>Available products</h2>
              </div>
              <span>{products.length} listing(s)</span>
            </div>
            <div className="product-grid">
              {products.map((p) => (
                <article className="product-card" key={p.productId}>
                  <div className="product-image">
                    {p.imageUrl ? (
                      <img src={p.imageUrl} alt={p.productName} />
                    ) : (
                      <span>🌾</span>
                    )}
                  </div>
                  <div className="product-body">
                    <span className="tag">{p.category || "Farm produce"}</span>
                    <h3>{p.productName}</h3>
                    <p className="description">
                      {p.description ||
                        "Fresh agricultural produce from local farms."}
                    </p>
                    <p className="price">
                      ₹{p.price} <small>/ {p.unit || "unit"}</small>
                    </p>
                    <p className="stock">
                      Available: {p.quantity} {p.unit}
                    </p>
                    <p className="farmer">
                      Farmer: {p.farmer?.fullName || "Local farmer"}
                    </p>
                    <div className="actions">
                      <button className="primary" onClick={() => buy(p)}>
                        Order
                      </button>
                      <button
                        className="secondary"
                        onClick={() => removeProduct(p.productId)}
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </article>
              ))}
            </div>
            {products.length === 0 && (
              <p className="empty">
                No products yet. Register a farmer and add the first listing
                below.
              </p>
            )}
            <section className="panel">
              <h2>Add a product (development mode)</h2>
              <p className="hint">
                Register as FARMER first, then enter your numeric user ID.
                Product endpoints are not yet role-protected; use only local
                development data.
              </p>
              <form className="form-grid" onSubmit={submitProduct}>
                <label>
                  Product name
                  <input
                    required
                    value={form.productName}
                    onChange={(e) =>
                      setForm({ ...form, productName: e.target.value })
                    }
                  />
                </label>
                <label>
                  Category
                  <select
                    value={form.category}
                    onChange={(e) =>
                      setForm({ ...form, category: e.target.value })
                    }
                  >
                    <option>Vegetables</option>
                    <option>Fruits</option>
                    <option>Grains</option>
                    <option>Pulses</option>
                    <option>Dairy</option>
                    <option>Other</option>
                  </select>
                </label>
                <label>
                  Price (₹ per unit)
                  <input
                    required
                    type="number"
                    min="0"
                    step="0.01"
                    value={form.price}
                    onChange={(e) =>
                      setForm({ ...form, price: e.target.value })
                    }
                  />
                </label>
                <label>
                  Quantity
                  <input
                    required
                    type="number"
                    min="0"
                    step="0.01"
                    value={form.quantity}
                    onChange={(e) =>
                      setForm({ ...form, quantity: e.target.value })
                    }
                  />
                </label>
                <label>
                  Unit
                  <select
                    value={form.unit}
                    onChange={(e) => setForm({ ...form, unit: e.target.value })}
                  >
                    <option>kg</option>
                    <option>quintal</option>
                    <option>bag</option>
                    <option>piece</option>
                    <option>litre</option>
                  </select>
                </label>
                <label>
                  Farmer user ID
                  <input
                    required
                    type="number"
                    min="1"
                    value={form.farmer.userId || (user?.role === "FARMER" ? user.userId : "")}
                    onChange={(e) =>
                      setForm({ ...form, farmer: { userId: e.target.value } })
                    }
                  />
                </label>
                <label className="wide">
                  Description
                  <input
                    value={form.description}
                    onChange={(e) =>
                      setForm({ ...form, description: e.target.value })
                    }
                  />
                </label>
                <label className="wide">
                  Image URL (optional)
                  <input
                    value={form.imageUrl}
                    onChange={(e) =>
                      setForm({ ...form, imageUrl: e.target.value })
                    }
                  />
                </label>
                <button className="primary wide" type="submit">
                  Add product
                </button>
              </form>
            </section>
          </>
        )}
        {view === "register" && (
          <section className="panel narrow">
            <p className="eyebrow">JOIN AGRIMARKET</p>
            <h2>Create an account</h2>
            <form className="stack" onSubmit={register}>
              <label>
                Full name
                <input
                  required
                  value={account.fullName}
                  onChange={(e) =>
                    setAccount({ ...account, fullName: e.target.value })
                  }
                />
              </label>
              <label>
                Email
                <input
                  required
                  type="email"
                  value={account.email}
                  onChange={(e) =>
                    setAccount({ ...account, email: e.target.value })
                  }
                />
              </label>
              <label>
                Password
                <input
                  required
                  type="password"
                  minLength="8"
                  value={account.password}
                  onChange={(e) =>
                    setAccount({ ...account, password: e.target.value })
                  }
                />
              </label>
              <label>
                Phone
                <input
                  value={account.phone}
                  onChange={(e) =>
                    setAccount({ ...account, phone: e.target.value })
                  }
                />
              </label>
              <label>
                Address
                <input
                  value={account.address}
                  onChange={(e) =>
                    setAccount({ ...account, address: e.target.value })
                  }
                />
              </label>
              <label>
                Register as
                <select
                  value={account.role}
                  onChange={(e) =>
                    setAccount({ ...account, role: e.target.value })
                  }
                >
                  <option value="FARMER">Farmer</option>
                  <option value="CUSTOMER">Customer</option>
                </select>
              </label>
              <button className="primary">Create account</button>
            </form>
          </section>
        )}
        {view === "login" && (
          <section className="panel narrow">
            <p className="eyebrow">WELCOME BACK</p>
            <h2>Log in</h2>
            <form className="stack" onSubmit={signIn}>
              <label>
                Email
                <input
                  required
                  type="email"
                  value={login.email}
                  onChange={(e) =>
                    setLogin({ ...login, email: e.target.value })
                  }
                />
              </label>
              <label>
                Password
                <input
                  required
                  type="password"
                  value={login.password}
                  onChange={(e) =>
                    setLogin({ ...login, password: e.target.value })
                  }
                />
              </label>
              <button className="primary">Log in</button>
            </form>
          </section>
        )}
        {view === "dashboard" && user && (
          <section className="panel">
            <p className="eyebrow">YOUR SPACE</p>
            <h2>
              {user.role === "FARMER"
                ? "Farmer dashboard"
                : "Customer dashboard"}
            </h2>
            <p>Welcome, {user.fullName}.</p>
            {dashboard ? (
              <div className="stats">
                {Object.entries(dashboard)
                  .filter(([k, v]) => typeof v !== "object")
                  .map(([k, v]) => (
                    <div className="stat" key={k}>
                      <span>{k.replace(/([A-Z])/g, " $1")}</span>
                      <strong>{String(v)}</strong>
                    </div>
                  ))}
              </div>
            ) : (
              <button className="primary" onClick={() => loadDashboard(user)}>
                Load dashboard
              </button>
            )}
            {user.role === "CUSTOMER" && (
              <button
                className="secondary"
                onClick={async () => {
                  try {
                    const r = await api.get(`/orders/customer/${user.userId}`);
                    setDashboard({ ...dashboard, orders: r.data });
                  } catch (e) {
                    setMessage("Could not load orders.");
                  }
                }}
              >
                Refresh order history
              </button>
            )}
            {dashboard?.orders && (
              <pre className="json">
                {JSON.stringify(dashboard.orders, null, 2)}
              </pre>
            )}
            <button
              className="secondary"
              onClick={() => {
                setUser(null);
                setDashboard(null);
                setView("market");
              }}
            >
              Log out
            </button>
          </section>
        )}
      </main>
      <footer>© 2026 AgriMarket · Supporting local agriculture</footer>
    </div>
  );
}
export default App;
