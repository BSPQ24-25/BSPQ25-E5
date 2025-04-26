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

// Funtion to load rooms
async function loadRooms() {
    try {
        const response = await fetch('/api/rooms');
        const rooms = await response.json();
        const contentDiv = document.querySelector('.content');

        const roomList = document.createElement('ul');
        roomList.className = 'list';

        rooms.forEach(room => {
            const li = document.createElement('li');

            li.innerHTML = `
          <div style="display: flex; align-items: center;">
            <div style="
              width: 200px;
              height: 100px;
              background-image: url('images/textarea-bg.gif');
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-position: center;
              display: flex;
              justify-content: center;
              align-items: center;
              font-weight: bold;
              font-size: 18px;
              color: #fff;
              margin-right: 20px;
            ">
              ${room.name}
            </div>
            <div>
              <ul style="list-style: disc; padding-left: 20px;">
                <li style="padding-top: 0px;"><strong>Seats:</strong> ${room.seatCount}</li>
                <li style="padding-top: 0px;">Free seats: ${room.availableSeats}</li>
                <li style="padding-top: 0px;">Reserved seats: ${room.reservedSeats}</li>
              </ul>
            </div>
          </div>
        `;
            roomList.appendChild(li);
        });

        // Reemplaza el contenido actual
        contentDiv.innerHTML = '<h3>Available <span>Rooms</span></h3>';
        contentDiv.appendChild(roomList);
    } catch (error) {
        console.error('Error loading rooms:', error);
    }
}

