window.addEventListener('load', function() {
  const token        = localStorage.getItem('token');
  const role         = localStorage.getItem('role');
  const estudianteId = localStorage.getItem('estudianteId');

  if (!token || role !== 'ROLE_USER') {
    window.location.href = 'http://127.0.0.1:5500/html/login.html';
    return;
  }

  // Muestra el nombre del usuario en navbar
  document.getElementById('estudianteUsername').textContent = localStorage.getItem('username');

  if (!estudianteId) {
    document.getElementById('tablero').innerHTML = `
      <div class="empty-state">
        <div style="font-size:2rem">⚠️</div>
        <p>Tu cuenta no tiene un perfil de estudiante asociado.<br>Contacta al administrador.</p>
      </div>`;
    return;
  }

  cargarTablero(estudianteId);
});

function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  };
}

async function cargarTablero(estudianteId) {
  try {
    // Carga estudiante
    let estudiante;
    try {
      const resEst = await fetch(`${API}/api/estudiantes/${estudianteId}`, { headers: authHeaders() });
      if (!resEst.ok) throw new Error('No se pudo cargar el estudiante');
      estudiante = await resEst.json();
    } catch (err) {
      console.error('Error estudiante:', err);
      document.getElementById('tablero').innerHTML = `
        <div class="empty-state">
          <div style="font-size:2rem">❌</div>
          <p>Error al cargar tu perfil de estudiante.</p>
        </div>`;
      return;
    }

    // Carga tablero
    let tablero = [];
    try {
      const resTablero = await fetch(`${API}/api/evaluaciones/tablero/${estudianteId}`, { headers: authHeaders() });
      if (!resTablero.ok) throw new Error('No se pudo cargar el tablero');
      tablero = await resTablero.json();
    } catch (err) {
      console.error('Error tablero:', err);
      tablero = [];
    }

    // Renderiza todo
    document.getElementById('welcomeNombre').textContent  = `Hola, ${estudiante.nombre}`;
    document.getElementById('welcomeCorreo').textContent  = estudiante.correo;
    renderTablero(tablero);

  } catch (err) {
    console.error(err);
    document.getElementById('tablero').innerHTML = `
      <div class="empty-state">
        <div style="font-size:2rem">❌</div>
        <p>Error inesperado. Intenta recargar la página.</p>
      </div>`;
  }
}


function renderTablero(tablero) {
  const container = document.getElementById('tablero');

  if (!tablero.length) {
    container.innerHTML = `
      <div class="empty-state">
        <div style="font-size:2rem">📭</div>
        <p>Aún no estás inscrito en ningún curso.</p>
      </div>`;
    return;
  }

  container.innerHTML = tablero.map((curso, index) => {
    const evals    = curso.evaluaciones || [];
    const promedio = evals.length
      ? (evals.reduce((sum, e) => sum + e.puntuacion, 0) / evals.length).toFixed(1)
      : null;

    return `
      <div class="curso-card" style="animation-delay: ${index * 0.08}s">
        <div class="curso-header" onclick="toggleCurso(${curso.cursoId})">
          <div class="curso-nombre">
            <div class="curso-icon">📚</div>
            <h3>${curso.nombreCurso}</h3>
          </div>
          <div class="curso-stats">
            <span class="stat-badge">
              <span>${evals.length}</span> evaluación${evals.length !== 1 ? 'es' : ''}
            </span>
            ${promedio !== null ? `
              <span class="stat-badge">
                Promedio: <span>${promedio}</span>
              </span>` : ''}
            <span class="chevron" id="chevron-${curso.cursoId}">▼</span>
          </div>
        </div>

        <div class="evaluaciones-body" id="eval-${curso.cursoId}">
          ${evals.length === 0 ? `
            <p class="empty-eval">Sin evaluaciones registradas aún.</p>
          ` : `
            <table>
              <thead>
                <tr>
                  <th>Evaluación</th>
                  <th>Puntuación</th>
                </tr>
              </thead>
              <tbody>
                ${evals.map(e => `
                  <tr>
                    <td>${e.evaluacionNombre}</td>
                    <td>${badgePuntuacion(e.puntuacion)}</td>
                  </tr>`).join('')}
                ${promedio !== null ? `
                  <tr class="promedio-row">
                    <td>Promedio del curso</td>
                    <td class="promedio-valor">${promedio}</td>
                  </tr>` : ''}
              </tbody>
            </table>
          `}
        </div>
      </div>`;
  }).join('');
}

function toggleCurso(cursoId) {
  const body    = document.getElementById(`eval-${cursoId}`);
  const chevron = document.getElementById(`chevron-${cursoId}`);
  body.classList.toggle('open');
  chevron.classList.toggle('open');
}

function badgePuntuacion(p) {
  if (p >= 70) return `<span class="puntuacion-badge puntuacion-alta">${p}</span>`;
  if (p >= 50) return `<span class="puntuacion-badge puntuacion-media">${p}</span>`;
  return `<span class="puntuacion-badge puntuacion-baja">${p}</span>`;
}