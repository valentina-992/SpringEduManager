const API = 'http://localhost:9095';

async function handleLogin() {
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value.trim();
  const btn      = document.getElementById('btnLogin');
  const errorMsg = document.getElementById('errorMsg');

  errorMsg.classList.remove('visible');

  if (!username || !password) {
    showError('Completa todos los campos.');
    return;
  }

  btn.disabled = true;
  btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span>Ingresando...';

  try {
    const res = await fetch(`${API}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });

    if (!res.ok) throw new Error('Credenciales inválidas');

    const data = await res.json();

    localStorage.setItem('token',    data.token);
    localStorage.setItem('username', data.username);
    localStorage.setItem('role',     data.role[0]);

    if (data.estudianteId) {
      localStorage.setItem('estudianteId', data.estudianteId);
    }
    
    redirectByRole(data.role[0]);

  } catch (err) {
    showError('Usuario o contraseña incorrectos. Intenta nuevamente.');
  } finally {
    btn.disabled = false;
    btn.innerHTML = 'Ingresar';
  }
}

function redirectByRole(role) {
  if (role === 'ROLE_ADMIN') {
    window.location.href = 'http://127.0.0.1:5500/html/admin.html';
  } else {
    window.location.href = 'http://127.0.0.1:5500/html/estudiante.html';
  }
}

function showError(msg) {
  const el = document.getElementById('errorMsg');
  el.textContent = msg;
  el.classList.add('visible');
}

function logout() {
  localStorage.clear();
  window.location.href = 'http://127.0.0.1:5500/html/login.html';
}

// Login con Enter
document.addEventListener('keydown', e => {
  if (e.key === 'Enter') {
    e.preventDefault();
    handleLogin();
  }
});

// Solo redirige automáticamente si estamos en login.html
if (window.location.pathname.includes('login.html')) {
  const token = localStorage.getItem('token');
  const role  = localStorage.getItem('role');
  if (token && role) redirectByRole(role);
}