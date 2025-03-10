#ifndef MEDS_H
#define MEDS_H

typedef struct meds{
    int id;
    char* name;
    int concentration;
    int quantity;

    // functions
    void (*change_quantity)(struct meds* Med, int number);
    _Bool (*equal)(const struct meds* Med1, const struct meds* Med2);
    void (*set_name)(struct meds* Med, const char *name);
} meds_t;

/// !MOST IMPORTANT FUNCTION!
/// sets the function pointers in the med_t
/// :param Med: meds_t* to witch we set the pointers
/// :return: NULL
void initialization(meds_t* Med);

/// sets the meds quantity to a different number
/// :param Med: meds_t* the med to witch we change the quantity
/// :param number: int
/// :return: NULL
void change_quantity(meds_t* Med, int number);

/// verify if two meds_t are equal
/// :param Med1: meds_t* the function caller
/// :param Med2: meds_t*
/// :return: _Bool
_Bool equal(const meds_t* Med1, const meds_t* Med2);

/// sets the name of the meds_t*
/// :param Med: meds_t* to whom we set
/// :param name: char* to what we set
/// :return: NULL
void set_name(meds_t* Med, const char* name);

#endif // MEDS_H
