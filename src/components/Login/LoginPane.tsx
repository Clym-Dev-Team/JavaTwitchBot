import {useForm} from "react-hook-form";
import "./LoginPane.css"

export interface LoginPaneProps {
  onSubmit: (login: Login) => void,
}

export interface Login {
  username: string,
  password: string,
}

export default function LoginPane({onSubmit}: LoginPaneProps) {
  const {register, handleSubmit} = useForm<Login>()

  return <div className="login">
    <form onSubmit={handleSubmit(onSubmit)} className="loginForm">
      <label className="loginForm-label">
        Username:
        <input {...register("username")}/>
      </label>
      <label className="loginForm-label">
        Password:
        <input {...register("password")}/>
      </label>
      <button className="loginForm-login" type="submit">Login</button>
    </form>
  </div>
}