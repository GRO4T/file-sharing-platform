import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { AuthProvider } from "react-oidc-context";
import App from "./App.tsx";

const onSigninCallback = (): void => {
  window.history.replaceState({}, document.title, window.location.pathname);
};

const oidcConfig = {
  authority: "http://localhost:8082/realms/myrealm",
  client_id: "myclient",
  redirect_uri: "http://localhost:5173",
  onSigninCallback: onSigninCallback,
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <App />
    </AuthProvider>
  </StrictMode>,
);
