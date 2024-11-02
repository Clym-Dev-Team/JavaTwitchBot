import {fetchWithAuth, useAuth} from "../components/Login/AuthProvider.tsx";
import {useCallback, useEffect, useState} from "react";
import {useToast} from "@shadcn/components/ui/use-toast.ts";
import {BOT_BACKEND_ADDR} from "../main.tsx";

export default function useData<T>(urlPath: string, objectName: string, initialValue: T, init?: RequestInit) {
  const {toast} = useToast();
  const authContext = useAuth();
  const [data, setData] = useState<T>(initialValue);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false)

  const get = useCallback(() => {
    if (error) {
      return;
    }
    let ignore = false;
    fetchWithAuth(authContext, BOT_BACKEND_ADDR + urlPath, init).then()
      .then(response => response.json())
      .then(json => {
        if (!ignore) {
          setData(json);
          setLoading(false);
        }
      })
      .catch(reason => toast({
        className: "toast toast-failure",
        title: "ERROR loading " + objectName,
        description: reason.toString()
      }))
      .catch(() => setLoading(false))
      .catch(() => setError(true))
    return () => {
      ignore = true;
    };

  }, [authContext, error, init, objectName, toast, urlPath]);

  useEffect(() => {
    get()
  }, [get]);

  const sendData = useCallback((urlPath: string, successToast: string, init?: RequestInit) => {
    fetchWithAuth(authContext, BOT_BACKEND_ADDR + urlPath, init).then()
      .then(() => toast({className: "toast toast-success", title: successToast}))
      .then(get)
      .catch(reason => toast({
        className: "toast toast-failure",
        title: "ERROR saving " + objectName,
        description: reason.toString()
      }))
  }, [authContext, objectName, toast]);

  return {data, loading, sendData};
}