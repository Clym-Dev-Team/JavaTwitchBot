import {createContext, PropsWithChildren, useContext, useState} from "react";
import LoginPane, {Login} from "./LoginPane.tsx"
import {sha3_512} from "js-sha3";

export interface AuthContext {
  accessToken?: string;
  setAccessToken: (accessToken: string | undefined) => void;
}

const AuthContext = createContext<AuthContext>({
  accessToken: getAuthFromCookies(),
  setAccessToken: (accessToken: string | undefined) => {
  }
});

export const useAuth = () => {
  return useContext(AuthContext);
}

export function getAuthFromCookies(): string | undefined {
  const token = localStorage.getItem("accessToken");
  if (token == null) {
    return undefined;
  }
  return token;
}


export default function AuthProvider(props: PropsWithChildren<Record<never, never>>) {
  const [accessToken, setAccessToken] = useState<string | undefined>(getAuthFromCookies);

  interface LoginRequest {
    username: string,
    hash: string,
    alg: string
  }

  function handleSubmit(login: Login) {
    const request: LoginRequest = {
      username: login.username,
      hash: sha3_512(login.password),
      alg: "SHA3-512"
    }
    fetch("http://localhost:80/login", {method: "POST", body: JSON.stringify(request)}).then()
      .then(res => res.text()
        .then(value => {
          setAccessToken(value);
          localStorage.setItem("accessToken", JSON.stringify(accessToken));
        }))
      .catch(err => console.log(err));
  }

  return <AuthContext.Provider value={{accessToken, setAccessToken: setAccessToken}}>
    {accessToken == undefined ? <LoginPane onSubmit={handleSubmit}/> : props.children}
  </AuthContext.Provider>
}

export async function fetchWithAuth(context: AuthContext, input: RequestInfo | URL, init?: RequestInit,): Promise<Response> {
  const {accessToken, setAccessToken} = context;

  if (accessToken == undefined) {
    return Promise.reject(new Error("Token invalid or Timed out. New Login required!"));
  }

  if (init == undefined) {
    init = {headers: [["token", accessToken]]};
  } else {
    const headers: Headers = new Headers(init.headers!);
    headers.set("token", accessToken);
  }

  const res = await fetch(input, init);
  if (res.status == 401) {
    setAccessToken(undefined);
    return Promise.reject(new Error("Token invalid or Timed out. New Login required!"));
  }
  if (res.status == 403) {
    return Promise.reject(new Error("No Permission to execute this action"));
  }
  return res;
}