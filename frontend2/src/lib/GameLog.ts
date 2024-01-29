export interface LogEntry {
index:number, playerName:string, actionName:string, actionValue:string
}

export type Log = LogEntry[];

export function event(log: Log): LogEntry | undefined {
    if (log) {
        return log[0];
    }
}