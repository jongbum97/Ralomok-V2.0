const { VITE_BACKEND_SOCKET_URL } = import.meta.env;

let socket = null;

function open(onOpen, onMessage, onClose, onError) {
  socket = new WebSocket(VITE_BACKEND_SOCKET_URL);

  socket.onopen = onOpen;

  socket.onmessage = onMessage;

  socket.onclose = onClose;

  socket.onerror = onError;
}

function close() {
  socket.close();
}

function send(object) {
  socket.send(JSON.stringify(object));
}

export { open, close, send };
