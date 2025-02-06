// // Функции для переключения между формами
// function showLoginForm() {
// document.getElementById('signup-form').style.display = 'none';    document.getElementById('login-form').style.display = 'block';
// }
// function showSignUpForm() {    document.getElementById('login-form').style.display = 'none';
//     document.getElementById('signup-form').style.display = 'block';}
// // Обработка регистрации
// async function handleSignUp(event) {    event.preventDefault();
//     const email = document.getElementById('signup-email').value;
//     const password = document.getElementById('signup-password').value;
//     try {        const response = await fetch('/api/students/register', {
//         method: 'POST',            headers: {
//             'Content-Type': 'application/json',            },
//         body: JSON.stringify({ email, password })        });
//         const data = await response.json();
//         if (data.success) {
//             localStorage.setItem('token', data.token);            showLoginForm(); // Перенаправление на форму входа
//         } else {            document.getElementById('signup-email-error').textContent = data.message;
//             document.getElementById('signup-email-error').style.display = 'block';        }
//     } catch (error) {        console.error('Registration error:', error);
//     }}
// // Обработка входа
// async function handleLogin(event) {    event.preventDefault();
//     const email = document.getElementById('login-email').value;
//     const password = document.getElementById('login-password').value;
//     try {        const response = await fetch('/api/students/login', {
//         method: 'POST',            headers: {
//             'Content-Type': 'application/json',            },
//         body: JSON.stringify({ email, password })        });
//         const data = await response.json();
//         if (data.success) {
//             localStorage.setItem('token', data.token);            window.location.href = '/home'; // Перенаправление на домашнюю страницу
//         } else {            document.getElementById('login-email-error').textContent = data.message;
//             document.getElementById('login-email-error').style.display = 'block';        }
//     } catch (error) {        console.error('Login error:', error);
//     }}








// document.addEventListener('DOMContentLoaded', function () {
//     const registerForm = document.getElementById('registerForm');
//     const loginForm = document.getElementById('loginForm');
//     const errorMessage = document.getElementById('errorMessage');
//     const successMessage = document.getElementById('successMessage');
//
//     registerForm.addEventListener('submit', async (e) => {
//         e.preventDefault();
//         const email = document.getElementById('registerEmail').value;
//         const password = document.getElementById('registerPassword').value;
//
//         const student = { email, password };
//
//         try {
//             const response = await fetch('http://localhost:7070/api/students/register', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json',
//                 },
//                 body: JSON.stringify(student),
//             });
//
//             if (!response.ok) {
//                 const error = await response.json();
//                 throw new Error(error.message || 'Registration failed');
//             }
//
//             const data = await response.json();
//             localStorage.setItem('token', data.token); // Сохраняем токен в localStorage
//             successMessage.textContent = 'Registration successful!';
//             successMessage.style.display = 'block';
//         } catch (error) {
//             errorMessage.textContent = error.message;
//             errorMessage.style.display = 'block';
//         }
//     });
//
//     loginForm.addEventListener('submit', async (e) => {
//         e.preventDefault();
//         const email = document.getElementById('loginEmail').value;
//         const password = document.getElementById('loginPassword').value;
//
//         const credentials = { email, password };
//
//         try {
//             const response = await fetch('http://localhost:7070/api/students/login', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json',
//                 },
//                 body: JSON.stringify(credentials),
//             });
//
//             if (!response.ok) {
//                 const error = await response.json();
//                 throw new Error(error.message || 'Login failed');
//             }
//
//             const data = await response.json();
//             localStorage.setItem('token', data.token); // Сохраняем токен в localStorage
//             successMessage.textContent = 'Login successful!';
//             successMessage.style.display = 'block';
//         } catch (error) {
//             errorMessage.textContent = error.message;
//             errorMessage.style.display = 'block';
//         }
//     });
// });
