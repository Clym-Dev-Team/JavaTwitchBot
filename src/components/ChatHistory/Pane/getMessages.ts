import {Message} from "./Message.ts";

export async function loadMore(timeStamp: number): Promise<Message[]> {
  console.log("fetching messages...");
  const response = await fetch(
    `http://localhost:8080/messages/earlier?timeStamp=${timeStamp}`
  );
  return await response.json() as Message[];
}