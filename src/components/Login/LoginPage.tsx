import {PropsWithChildren, useState} from "react";
import LoginPane, {Login} from "./LoginPane.tsx"
import {sha3_512} from "js-sha3";
import {BOT_BACKEND_ADDR} from "../../main.tsx";

function getAuthFromCookies(): string | undefined {
  const token = localStorage.getItem("accessToken");
  if (token == null) {
    return undefined;
  }
  return token;
}

export default function LoginPage(props: PropsWithChildren<Record<never, never>>) {
  const [accessToken, setAccessToken] = useState<string | undefined>(getAuthFromCookies());

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
    fetch(`${BOT_BACKEND_ADDR}/login`, {method: "POST", body: JSON.stringify(request)}).then()
      .then(res => res.text()
        .then(value => {
          setAccessToken(value);
          localStorage.setItem("accessToken", value);
        }))
      .catch(err => console.log(err));
  }

  if (accessToken == undefined) {
    return <LoginPane onSubmit={handleSubmit}/>;
  }
  return props.children;
}

export async function fetchWithAuth(input: RequestInfo | URL, init?: RequestInit): Promise<Response> {
  const accessToken = getAuthFromCookies();

  if (accessToken == undefined) {
    return Promise.reject(new Error("Token invalid or Timed out. Reload the page and login again!"));
  }

  if (init === undefined) {
    init = {headers: [["token", accessToken]]};
  } else if (init.headers === undefined) {
    init.headers = [["token", accessToken]]
  } else {
    const headers: Headers = new Headers(init.headers!);
    headers.set("token", accessToken);
    init.headers = headers;
  }

  const res = await fetch(input, init);
  if (res.ok) {
    return res;
  } else if (res.status == 401) {
    localStorage.removeItem("accessToken");
    return Promise.reject(new Error("Token invalid or Timed out. New Login required!"));
  } else if (res.status == 403) {
    return Promise.reject(new Error("No Permission to execute this action"));
  } else {
    return Promise.reject(new Error(`${res.status} ${res.statusText}`));
  }
}