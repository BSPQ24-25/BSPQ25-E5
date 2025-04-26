// Function to handle login form submission
document.getElementById("loginForm")?.addEventListener("submit", async function (e) {
    e.preventDefault(); // Prevent the default form submission

    console.log("Form submitted!"); // Log this message to confirm the event handler is working

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            throw new Error("Invalid credentials");
        }

        const token = await response.text();
        localStorage.setItem("token", token);

        console.log("Redirecting to /home"); // Log before redirecting

        window.location.href = "/home"; // Redirect to home page
    } catch (error) {
        document.getElementById("errorMsg").innerText = error.message;
    }
});

// Function to handle registration form submission
document.getElementById("registerForm")?.addEventListener("submit", async function (e) {
    e.preventDefault();

    const data = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value
    };

    try {
        const response = await fetch("/api/users/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        window.location.href = "/"; // Redirect to login page after successful registration
    } catch (err) {
        alert(err.message);
    }
});
