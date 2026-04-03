// Protege la página — si no hay token o no es admin, redirige

/* (function checkAuth() {
  const token = localStorage.getItem('token');
  const role  = localStorage.getItem('role');
  */

window.addEventListener('load', function() {
  const token = localStorage.getItem('token');
  const role  = localStorage.getItem('role');

  if (!token || role !== 'ROLE_ADMIN') {
    window.location.href = 'http://127.0.0.1:5500/html/login.html';
    return;
  }

  document.getElementById('adminUsername').textContent = localStorage.getItem('username');
  showSection('sec-usuarios');
});

// Headers con token
function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  };
}

// ── Navegación sidebar ──────────────────────────────────────────
function showSection(id) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.sidebar-item').forEach(s => s.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  document.querySelector(`[data-section="${id}"]`).classList.add('active');

  // Carga datos al cambiar sección
  if (id === 'sec-usuarios')     cargarUsuarios();
  if (id === 'sec-cursos')       cargarCursos();
  if (id === 'sec-estudiantes')  cargarEstudiantes();
  if (id === 'sec-inscripciones') { cargarInscripciones(); cargarSelectsInscripcion(); }
  if (id === 'sec-evaluaciones') { cargarEvaluaciones(); cargarSelectsEvaluacion(); }
}

// ── USUARIOS ────────────────────────────────────────────────────
async function crearUsuario() {
  const body = {
    username: document.getElementById('u-username').value.trim(),
    password: document.getElementById('u-password').value.trim(),
    role:     document.getElementById('u-role').value,
    email:    document.getElementById('u-email').value.trim(),
    nombre:   document.getElementById('u-nombre').value.trim()
  };

  if (!body.username || !body.password || !body.email || !body.nombre) {
    return showAlert('u-alert', 'error', 'Completa todos los campos.');
  }

  try {
    const res = await fetch(`${API}/api/usuarios`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify(body)
    });
    if (!res.ok) throw new Error();
    showAlert('u-alert', 'success', 'Usuario creado correctamente.');
    document.getElementById('form-usuario').reset();
    cargarUsuarios();
  } catch {
    showAlert('u-alert', 'error', 'Error al crear el usuario.');
  }
}

/* async function cargarUsuarios() {
  try {
    const res = await fetch(`${API}/api/usuarios`, { headers: authHeaders() });
    const data = await res.json();
    const tbody = document.getElementById('tabla-usuarios');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="5" class="empty-state">No hay usuarios registrados.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(u => `
      <tr>
        <td>${u.id}</td>
        <td>${u.nombre ?? '—'}</td>
        <td>${u.username}</td>
        <td>${u.email}</td>
        <td><span class="badge-role ${u.role === 'ROLE_ADMIN' ? 'badge-admin' : 'badge-user'}">${u.role}</span></td>
        <td><button class="btn-danger-custom" onclick="eliminarUsuario(${u.id})">Eliminar</button></td>
      </tr>`).join('');
  } catch {
    console.error('Error al cargar usuarios');
  }
}
  */

async function cargarUsuarios() {
  try {
    const res = await fetch(`${API}/api/usuarios`, { headers: authHeaders() });
    console.log('Status:', res.status);
    const text = await res.text();
    console.log('Response:', text);
    const data = JSON.parse(text);
    const tbody = document.getElementById('tabla-usuarios');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="6" class="empty-state">No hay usuarios registrados.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(u => `
      <tr>
        <td>${u.id}</td>
        <td>${u.nombre ?? '—'}</td>
        <td>${u.username}</td>
        <td>${u.email}</td>
        <td><span class="badge-role ${u.role === 'ADMIN' ? 'badge-admin' : 'badge-user'}">${u.role}</span></td>
        <td><button class="btn-danger-custom" onclick="eliminarUsuario(${u.id})">Eliminar</button></td>
      </tr>`).join('');
  } catch(e) {
    console.error('Error completo:', e);
  }
}

async function eliminarUsuario(id) {
  if (!confirm('¿Eliminar este usuario?')) return;
  try {
    await fetch(`${API}/api/usuarios/${id}`, { 
      method: 'DELETE', 
      headers: authHeaders()
    });
    cargarUsuarios();
  } catch {
    alert('Error al eliminar el usuario.');
  }
}

// ── CURSOS ──────────────────────────────────────────────────────
async function crearCurso() {
  const nombre = document.getElementById('c-nombre').value.trim();
  if (!nombre) return showAlert('c-alert', 'error', 'Ingresa el nombre del curso.');

  try {
    const res = await fetch(`${API}/api/cursos`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify({ nombre })
    });
    if (!res.ok) throw new Error();
    showAlert('c-alert', 'success', 'Curso creado correctamente.');
    document.getElementById('c-nombre').value = '';
    cargarCursos();
  } catch {
    showAlert('c-alert', 'error', 'Error al crear el curso.');
  }
}

async function cargarCursos() {
  try {
    const res = await fetch(`${API}/api/cursos`, { headers: authHeaders() });
    const data = await res.json();
    const tbody = document.getElementById('tabla-cursos');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="3" class="empty-state">No hay cursos registrados.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(c => `
      <tr>
        <td>${c.id}</td>
        <td>${c.nombre}</td>
        <td><button class="btn-danger-custom" onclick="eliminarCurso(${c.id})">Eliminar</button></td>
      </tr>`).join('');
  } catch {
    console.error('Error al cargar cursos');
  }
}

async function eliminarCurso(id) {
  if (!confirm('¿Eliminar este curso?')) return;
  try {
    await fetch(`${API}/api/cursos/${id}`, { method: 'DELETE', headers: authHeaders() });
    cargarCursos();
  } catch {
    alert('Error al eliminar el curso.');
  }
}

// ── ESTUDIANTES ─────────────────────────────────────────────────
async function cargarEstudiantes() {
  try {
    const res = await fetch(`${API}/api/estudiantes`, { headers: authHeaders() });
    const data = await res.json();
    const tbody = document.getElementById('tabla-estudiantes');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="4" class="empty-state">No hay estudiantes registrados.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(e => `
      <tr>
        <td>${e.id}</td>
        <td>${e.nombre}</td>
        <td>${e.correo}</td>
        <td><button class="btn-danger-custom" onclick="eliminarEstudiante(${e.id})">Eliminar</button></td>
      </tr>`).join('');
  } catch {
    console.error('Error al cargar estudiantes');
  }
}

async function eliminarEstudiante(id) {
  if (!confirm('¿Eliminar este estudiante?')) return;
  try {
    await fetch(`${API}/api/estudiantes/${id}`, { method: 'DELETE', headers: authHeaders() });
    cargarEstudiantes();
  } catch {
    alert('Error al eliminar el estudiante.');
  }
}

// ── INSCRIPCIONES ───────────────────────────────────────────────
async function cargarSelectsInscripcion() {
  try {
    const [resE, resC] = await Promise.all([
      fetch(`${API}/api/estudiantes`, { headers: authHeaders() }),
      fetch(`${API}/api/cursos`,      { headers: authHeaders() })
    ]);
    const estudiantes = await resE.json();
    const cursos      = await resC.json();

    document.getElementById('i-estudiante').innerHTML =
      '<option value="">Selecciona estudiante</option>' +
      estudiantes.map(e => `<option value="${e.id}">${e.nombre}</option>`).join('');

    document.getElementById('i-curso').innerHTML =
      '<option value="">Selecciona curso</option>' +
      cursos.map(c => `<option value="${c.id}">${c.nombre}</option>`).join('');
  } catch {
    console.error('Error al cargar selects de inscripción');
  }
}

async function crearInscripcion() {
  const estudianteId = document.getElementById('i-estudiante').value;
  const cursoId      = document.getElementById('i-curso').value;

  if (!estudianteId || !cursoId) {
    return showAlert('i-alert', 'error', 'Selecciona estudiante y curso.');
  }

  try {
    const res = await fetch(`${API}/api/inscripciones`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify({ estudianteId: Number(estudianteId), cursoId: Number(cursoId) })
    });
    if (!res.ok) throw new Error();
    showAlert('i-alert', 'success', 'Inscripción creada correctamente.');
    cargarInscripciones();
  } catch {
    showAlert('i-alert', 'error', 'Error al crear la inscripción.');
  }
}

async function cargarInscripciones() {
  try {
    const res = await fetch(`${API}/api/inscripciones`, { headers: authHeaders() });
    const data = await res.json();
    const tbody = document.getElementById('tabla-inscripciones');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="4" class="empty-state">No hay inscripciones registradas.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(i => `
      <tr>
        <td>${i.id}</td>
        <td>${i.estudianteId}</td>
        <td>${i.cursoId}</td>
        <td>${i.fecha_inscripcion}</td>
      </tr>`).join('');
  } catch {
    console.error('Error al cargar inscripciones');
  }
}

// ── EVALUACIONES ────────────────────────────────────────────────
async function cargarSelectsEvaluacion() {
  try {
    const res = await fetch(`${API}/api/inscripciones`, { headers: authHeaders() });
    const data = await res.json();

    document.getElementById('ev-inscripcion').innerHTML =
      '<option value="">Selecciona inscripción</option>' +
      data.map(i => `<option value="${i.id}">Inscripción #${i.id} — Est. ${i.estudianteId} / Curso ${i.cursoId}</option>`).join('');
  } catch {
    console.error('Error al cargar selects de evaluación');
  }
}

async function crearEvaluacion() {
  const inscripcionId = document.getElementById('ev-inscripcion').value;
  const nombre        = document.getElementById('ev-nombre').value.trim();
  const puntuacion    = parseFloat(document.getElementById('ev-puntuacion').value);

  if (!inscripcionId || !nombre || isNaN(puntuacion)) {
    return showAlert('ev-alert', 'error', 'Completa todos los campos.');
  }

  try {
    const res = await fetch(`${API}/api/evaluaciones/inscripcion/${inscripcionId}`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify({ nombre, puntuacion })
    });
    if (!res.ok) throw new Error();
    showAlert('ev-alert', 'success', 'Evaluación registrada correctamente.');
    document.getElementById('form-evaluacion').reset();
    cargarEvaluaciones();
  } catch {
    showAlert('ev-alert', 'error', 'Error al registrar la evaluación.');
  }
}

async function cargarEvaluaciones() {
  try {
    const res = await fetch(`${API}/api/evaluaciones`, { headers: authHeaders() });
    const data = await res.json();
    const tbody = document.getElementById('tabla-evaluaciones');

    if (!data.length) {
      tbody.innerHTML = '<tr><td colspan="4" class="empty-state">No hay evaluaciones registradas.</td></tr>';
      return;
    }

    tbody.innerHTML = data.map(e => `
      <tr>
        <td>${e.id}</td>
        <td>${e.nombre}</td>
        <td>${e.puntuacion}</td>
        <td>Inscripción #${e.inscripcionId}</td>
      </tr>`).join('');
  } catch {
    console.error('Error al cargar evaluaciones');
  }
}

// ── Utilidades ──────────────────────────────────────────────────
function showAlert(id, type, msg) {
  const el = document.getElementById(id);
  el.textContent = msg;
  el.className = type === 'success' ? 'alert-success visible' : 'alert-error visible';
  setTimeout(() => el.classList.remove('visible'), 4000);
}

// Carga inicial
// showSection('sec-usuarios');