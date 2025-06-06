#ifndef ERRORS_H
#define ERRORS_H
#include <exception>

namespace err {

	class Errors : public std::exception {
	protected:
		const char* error{};
		Errors() = default;
		explicit Errors(const char* error) : error(error) {}
	public:
		[[nodiscard]] const char* what() const noexcept override { return error; }
		~Errors() override = default;
	};

	class InvalidArgument final : public Errors {
	public:
		explicit InvalidArgument(const char* str) : Errors(str) {}
	};

	class LogicError final : public Errors {
	public:
		explicit LogicError(const char* str) : Errors(str) {}
	};

	class OutOfRange final : public Errors {
	public:
		explicit OutOfRange(const char* str) : Errors(str) {}
	};

}


#endif //ERRORS_H
