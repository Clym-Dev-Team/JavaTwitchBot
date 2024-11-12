
export enum HealthStatus {
  STOPPED,
  HEALTHY,
  STARTING,
  INJURED,
  DEAD
}

export interface InputHealth {
  name: string,
  status: HealthStatus,
  description: string,
}