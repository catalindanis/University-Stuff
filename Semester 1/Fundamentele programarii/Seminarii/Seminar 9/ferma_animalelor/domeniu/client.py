class Client:

    def __init__(self,id_client,nume):
        self.__id_client = id_client
        self.__nume = nume


    def get_id_client(self):
        return self.__id_client

    def get_nume(self):
        return self.__nume