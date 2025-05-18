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
        localStorage.setItem("username", username);
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


document.addEventListener("DOMContentLoaded", function () {

    const seats = document.querySelectorAll('.seat:not(.reserved)');
    const reserveButton = document.getElementById('reserveButton');
    const selectedSeatInput = document.getElementById('selectedSeatId');
    let selectedSeatId = null; // Variable global para almacenar el ID del asiento seleccionado

    // Maneja la selección del asiento
    seats.forEach(seat => {
        seat.addEventListener('click', function () {
            // Deselecciona otros asientos
            seats.forEach(s => s.classList.remove('selected'));

            // Marca el asiento clicado
            seat.classList.add('selected');

            // Guarda el seatId en la variable global
            selectedSeatId = seat.getAttribute('data-seat-id');
            selectedSeatInput.value = selectedSeatId;
            console.log("Selected seat ID:", selectedSeatInput.value); // Log the selected seat ID for debugging

            // Habilita el botón de reserva si se selecciona un asiento
            reserveButton.disabled = false;
        });
    });

    // Maneja la acción de clic en el botón de reserva
    reserveButton.addEventListener("click", function () {
        // Verifica si hay un asiento seleccionado
        if (!selectedSeatId) {
            alert("Please select a seat first.");
            return;
        }

        // Recupera el username desde localStorage
        const username = localStorage.getItem("username");

        if (!username) {
            alert("You must be logged in to reserve a seat.");
            return;
        }

        const screeningId = document.getElementById("screeningId").value;
        console.log("Screening ID:", screeningId); // Log the screening ID for debugging

        // Realiza la reserva a través de la API
        fetch("/api/reservations", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                "username": username,
                "screeningId": screeningId,
                "seatId": selectedSeatId
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    window.location.href = "/screenings"; // Redirige a la página de screenings
                } else {
                    alert("Failed to make reservation.");
                }
            })
            .catch(error => {
                console.error("Error making reservation:", error);
                alert("An error occurred. Please try again.");
            });
    });
});

document.addEventListener("DOMContentLoaded", function () {

    // === CANCEL RESERVATION ===
    document.querySelectorAll('.cancel-button').forEach(button => {
        button.addEventListener('click', async () => {
            const reservationId = button.getAttribute('data-id');
            if (!reservationId) {
                console.error("No reservation ID found.");
                return;
            }

            const confirmCancel = confirm("Are you sure you want to cancel this reservation?");
            if (!confirmCancel) return;

            try {
                const response = await fetch(`/api/reservations/${reservationId}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    location.reload();
                } else {
                    throw new Error("Failed to cancel reservation.");
                }
            } catch (error) {
                alert(error.message);
            }
        });
    });

    // === PAY RESERVATION ===
    document.querySelectorAll('.pay-button').forEach(button => {
        button.addEventListener('click', () => {
            const reservationId = button.getAttribute('data-id');
            if (!reservationId) {
                console.error("No reservation ID for payment.");
                return;
            }

            // Redirigir a la página de checkout
            window.location.href = `/payments/${reservationId}`;
        });
    });

});


document.addEventListener("DOMContentLoaded", function () {
    const paymentForm = document.getElementById("payment-form");

    if (paymentForm) {
        paymentForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            const reservationId = paymentForm.querySelector('input[name="reservationId"]').value;
            const amount = paymentForm.querySelector('input[name="amount"]').value;
            const paymentMethod = paymentForm.querySelector('select[name="paymentMethod"]').value;
            const date = new Date().toISOString(); // Fecha actual en formato ISO

            if (!paymentMethod) {
                alert("Please select a payment method.");
                return;
            }

            try {
                const response = await fetch("/payments/process", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: new URLSearchParams({
                        reservationId,
                        amount,
                        paymentMethod,
                        date
                    })
                });

                console.log("Response status:", response.status); // Log the response status for debugging

                if (response.ok) {
                    alert("Payment processed successfully!");
                    window.location.href = "/myreservations";
                } else {
                    const errorText = await response.text();
                    throw new Error(errorText || "Payment failed.");
                }
            } catch (error) {
                alert("Error: " + error.message);
            }
        });
    }
});