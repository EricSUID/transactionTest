package com.transaction.test.service;

public interface Example {
    void notransaction_exception_notransaction_notransaction();
    void notransaction_notransaction_notransaction_exception();
    void transaction_exception_notransaction_notransaction();
    void transaction_notransaction_notransaction_exception();
    void transaction_noTransaction_noTransaction_exception_try();
    /**
     * 没有事务注解。
     */
    public void notransaction_required_required();

    public void notransaction_exception_required_required();
    public void notransaction_required_required_exception();
    public void transaction_exception_required_required();
    public void transaction_required_required_exception();
    public void transaction_required_required_exception_try();



    void notransaction_supports_supports_exception();
    void notransaction_exception_supports_supports();
    void transaction_supports_supports_exception();
    void transaction_exception_supports_supports();



    void notransaction_exception_requiresNew_requiresNew();
    void notransaction_requiresNew_requiresNew_exception();
    void transaction_exception_required_requiresNew_requiresNew();
    void transaction_required_requiresNew_requiresNew_exception();
    void transaction_required_requiresNew_requiresNew_exception_try();



    void notransaction_exception_required_notSuppored();
    void notransaction_required_notSuppored_exception();
    void transaction_exception_required_notSuppored();
    void transaction_required_notSuppored_exception();



    void notransaction_mandatory();
    void transaction_exception_mandatory_mandatory();
    void transaction_mandatory_mandatory_exception();


    /*
     * PROPAGATION_NEVER
     */
    void transaction_never();
    void notransaction_exception_never_never();
    void notransaction_never_never_exception();



    /*
     * PROPAGATION_NESTED
     */
    void transaction_exception_nested_nested();
    void transaction_nested_nested_exception();
    void notransaction_exception_nested_nested();
    void notransaction_nested_nested_exception();
    void transaction_nested_nested_exception_try();



    void notransaction_addRequired_getRequired_get();
    void transaction_addRequired_getRequired_get();
    void transaction_addRequired_getNested_get();
    void transaction_addRequired_getNotSuppored_get();
    void transaction_addRequired_getRequiresNew_get();
}
