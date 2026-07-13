export interface AgendaBlockItemRequest {
  appointmentDate: string;
  startTime: string;
  endTime: string;
}

export interface CreateAgendaBlocksRequest {
  blocks: AgendaBlockItemRequest[];
}

export interface AgendaBlockResponse {
  id: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;
  available: boolean;
}

export interface CreateAgendaBlocksResponse {
  doctorId: number;
  medicalAgendaId: number;
  createdBlocks: AgendaBlockResponse[];
}