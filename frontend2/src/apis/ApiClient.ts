import axios, { AxiosInstance } from "axios";
import BankInfoRes from "./response/BankInfoRes";

const BASE_URL = "https://k7a403.p.ssafy.io/";
const API_PATH = "api/v1";

interface BankApi {
  getBankInfos(): Promise<BankInfoRes[]>;
  getFinanceBankInfos(): Promise<BankInfoRes[]>;
  remit(
    senderAccountName: String,
    senderAccountNumber: String,
    receiverAccountName: String,
    receiverAccountNumber: String,
    money: Number,
    token: Number
  ): Promise<void>;
  checkToken(token: Number): Promise<boolean>;
}

export default class ApiClient implements BankApi {
  private static instance: ApiClient;
  private axiosInstance: AxiosInstance;

  constructor() {
    this.axiosInstance = this.createAxiosInstance();
  }

  async checkToken(token: Number): Promise<boolean> {
    return (
      await this.axiosInstance.request({
        method: "GET",
        url: `${API_PATH}/remit/phone/nonmember/${token}`,
      })
    ).data.token;
  }

  async remit(
    senderAccountName: String,
    senderAccountNumber: String,
    receiverAccountName: String,
    receiverAccountNumber: String,
    money: Number,
    token: Number
  ): Promise<void> {
    console.log(`senderAccountName: ${senderAccountName}`);
    console.log(`senderAccountNumber: ${senderAccountNumber}`);
    console.log(`receiverAccountName: ${receiverAccountName}`);
    console.log(`receiverAccountNumber: ${receiverAccountNumber}`);
    console.log(`money: ${money}`);
    console.log(`token: ${token}`);
    return await this.axiosInstance.request({
      method: "POST",
      url: `${API_PATH}/remit/phone/nonmember`,
      data: {
        remit_nonmember_req: {
          remit_info_req: {
            ac_name: senderAccountName,
            ac_tag: receiverAccountName,
            ac_send: senderAccountNumber,
            ac_receive: receiverAccountNumber,
            value: money,
            receive: "",
            send: "",
          },
          remit_available_res: {
            token_id: token,
            token: false,
          },
        },
      },
    });
  }

  static getInstance(): ApiClient {
    return this.instance || (this.instance = new this());
  }

  async getBankInfos(): Promise<BankInfoRes[]> {
    return (
      await this.axiosInstance.request({
        method: "GET",
        url: `${API_PATH}/bank/info`,
      })
    ).data;
  }

  async getFinanceBankInfos(): Promise<BankInfoRes[]> {
    return (
      await this.axiosInstance.request({
        method: "GET",
        url: `${API_PATH}/bank/finance/info`,
      })
    ).data;
  }

  login(newToken: string) {
    this.axiosInstance = this.createAxiosInstance(newToken);
  }

  private createAxiosInstance = (token?: string) => {
    const headers: any = {
      "content-type": "application/json",
    };

    if (token) {
      headers["access_token"] = `Bearer ${token}`;
    }

    return axios.create({
      baseURL: BASE_URL,
      timeout: 1000,
      headers,
    });
  };
}
