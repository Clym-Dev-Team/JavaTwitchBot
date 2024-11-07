import useData from "../../common/useData.ts";
import Loader from "../../common/LoadingSpinner/Loader.tsx";
import {Button} from "@shadcn/components/ui/button.tsx";
import {DeleteIcon, SaveIcon} from "lucide-react";
import {ScrollArea} from "../ui/scroll-area.tsx";
import {Input} from "@shadcn/components/ui/input.tsx";
import {useState} from "react";

interface Account {
  username: string,
  userId: string,
  accountCreationTime: Date
}

export default function AccountsPage() {
  const {data, loading, sendData} = useData("/panelAccounts", "Panel Account", [])
  const [newUsername, setNewUsername] = useState<string>("")

  function onDelete(userId: string, username: string) {
    sendData("/panelAccounts", "Deleted " + username + " Successfully", {method: "DELETE", body: userId})
  }

  function onSubmit() {
    sendData("/panelAccounts", "Created " + newUsername + " Successfully", {method: "POST", body: newUsername})
  }

  if (loading)
    return <Loader/>

  return <div>
    <form onSubmit={() => onSubmit()}>
      <Input value={newUsername} onChange={event => setNewUsername(event.target.value)}/>
      <Button><SaveIcon/></Button>
    </form>
    <ScrollArea>
      {data.map((account: Account) =>
        <div key={account.userId}>
          <div>{account.username}</div>
          <Button onClick={() => onDelete(account.userId, account.username)}><DeleteIcon/></Button>
        </div>
      )}
    </ScrollArea>
  </div>
}