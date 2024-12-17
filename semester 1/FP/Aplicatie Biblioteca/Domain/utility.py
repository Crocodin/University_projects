import uuid

class Utility:

    @staticmethod
    def generate_id() -> str:
        """
        generates an id
        :return: string
        """
        return str(uuid.uuid4())

    @staticmethod
    def format_name(full_name: str) -> str:
        """ convert a str into a str of capitalized names """
        return ' '.join(word.capitalize() for word in full_name.split())