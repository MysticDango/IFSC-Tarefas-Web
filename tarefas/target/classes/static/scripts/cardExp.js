console.log('JS carregado!');

document.addEventListener('DOMContentLoaded', () => {
  const overlay = document.getElementById('overlay');

  document.querySelectorAll('.btn-expand').forEach(btn => {
    btn.addEventListener('click', (event) => {
      event.stopPropagation();

      const card = btn.closest('.task-card');
      const isExpanded = card.classList.contains('expanded');

      // Fechar todos os cards e remover overlay
      document.querySelectorAll('.task-card').forEach(c => c.classList.remove('expanded'));
      overlay.classList.remove('active');

      if (!isExpanded) {
        card.classList.add('expanded');
        overlay.classList.add('active');
      }
    });
  });

  // Fechar ao clicar no fundo escuro
  overlay.addEventListener('click', () => {
    document.querySelectorAll('.task-card').forEach(c => c.classList.remove('expanded'));
    overlay.classList.remove('active');
  });
});

