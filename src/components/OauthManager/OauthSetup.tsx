import {useEffect, useState} from "react";
import {useToast} from "@shadcn/components/ui/use-toast.ts";
import {getOauth} from "./GetOauth.ts";
import AuthTile from "./AuthTile.tsx";
import "./AuthList.css"
import {Loader} from "lucide-react";

export interface Oauth {
  service: string,
  accName: string,
  url: string,
}

export default function OauthSetup() {
  const toast = useToast();
  const [loading, setLoading] = useState(true);
  const [oauth, setOauth] = useState<Oauth[]>([])

  useEffect(() => {
    getOauth()
      .then(r => setOauth(r))
      .then(() => setLoading(false))
      .catch(reason => toast.toast(
        {className: "toast toast-failure", title: "ERROR Loading Oauth Status", description: reason.toString()}))
      .finally(() => setLoading(false));
  }, [])

  if (loading)
    return <Loader/>

  if (oauth == undefined || oauth.length == 0) {
    return <div className="authNotNeeded"><h1>All accounts are setup!</h1></div>
  }

  return <div className="authListContainer">
    <h1>External accounts that need to be setup:</h1>
    <div className="authList">
      {oauth.map((oauth, index) => <AuthTile key={index} oauth={oauth}/>)}
    </div>
  </div>
}