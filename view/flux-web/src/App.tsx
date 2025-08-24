import { useAuth } from "react-oidc-context";
import FluxAppBar from "./components/FluxAppBar";
import FileDrawer from "./components/FileDrawer";

export default function App() {
  const auth = useAuth();

  switch (auth.activeNavigator) {
    case "signinSilent":
      return <div>Signing you in...</div>;
    case "signoutRedirect":
      return <div>Signing you out...</div>;
  }

  if (auth.isLoading) {
    return <div>Loading...</div>;
  }

  if (auth.error) {
    return (
      <div>
        Oops... {auth.error.kind} caused {auth.error.message}
      </div>
    );
  }

  if (auth.isAuthenticated) {
    return (
      <>
        <FluxAppBar />
        <FileDrawer />
      </>
    );
  }

  return <button onClick={() => void auth.signinRedirect()}>Log in</button>;
}
