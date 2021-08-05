package com.transaction.test;

import com.transaction.test.service.impl.TransactionExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Eric
 * @description 外围方法：当前调用的方法, 即 TransactionExample.XXX_XXX_XXX_XXX();
 * PROPAGATION_REQUIRED
 * 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。这是最常见的选择。
 * <p>
 * PROPAGATION_SUPPORTS
 * 支持当前事务，如果当前没有事务，就以非事务方式执行。
 * <p>
 * PROPAGATION_MANDATORY
 * 使用当前的事务，如果当前没有事务，就抛出异常。
 * <p>
 * PROPAGATION_REQUIRES_NEW
 * 新建事务，如果当前存在事务，把当前事务挂起。
 * <p>
 * PROPAGATION_NOT_SUPPORTED
 * 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
 * <p>
 * PROPAGATION_NEVER
 * 以非事务方式执行，如果当前存在事务，则抛出异常。
 * <p>
 * PROPAGATION_NESTED
 * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。
 * @date 2021/04/30 22:42
 */
@SpringBootTest(classes = TransactionTestApplication.class)
@RunWith(SpringRunner.class)
public class TransactionTestApplicationTests {

    @Autowired
    private TransactionExample TransactionExample;


    /**
     * 结果：张三（插入），李四（插入）</br>
     */
    @Test
    public void notransaction_exception_notransaction_notransaction() {
        TransactionExample.notransaction_exception_notransaction_notransaction();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     */
    @Test
    public void notransaction_notransaction_notransaction_exception() {
        TransactionExample.notransaction_notransaction_notransaction_exception();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，外围方法内的方法就应该在同一个事务中。外围方法抛出异常，整个事务所有方法都会被回滚。
     */
    @Test
    public void transaction_exception_notransaction_notransaction() {
        TransactionExample.transaction_exception_notransaction_notransaction();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，外围方法内的方法就应该在同一个事务中。内部方法抛出异常，被外围方法捕获，整个事务中所有方法都会被回滚。
     */
    @Test
    public void transaction_notransaction_notransaction_exception() {
        TransactionExample.transaction_notransaction_notransaction_exception();
    }


    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法开启事务，内部方法都在同一事务中，只要不抛出异常，事务就不会回滚。
     */
    @Test
    public void transaction_noTransaction_noTransaction_exception_try() {
        TransactionExample.transaction_noTransaction_noTransaction_exception_try();
    }

    /**
     * 没有事务注解。
     * 结果：张三（插入），李四（插入）</br>
     * 观察方法执行，执行完插入“张三”方法后数据库即插入数据，执行完插入“李四”方法后数据库即插入数据，
     * 结合后面回滚方法，说明两个方法分别在两个事务中执行。
     */
    @Test
    public void notransaction_required_required() {
        TransactionExample.notransaction_required_required();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void notransaction_exception_required_required() {
//        “张三”、“李四”均插入。
//        外围方法未开启事务，插入“张三”、“李四”方法在自己的事务中独立运行，外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
        TransactionExample.notransaction_exception_required_required();
    }

    @Test
    public void notransaction_required_required_exception() {
//        “张三”插入，“李四”未插入。
//        外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,所以插入“李四”方法抛出异常只会回滚插入“李四”方法，插入“张三”方法不受影响。
        TransactionExample.notransaction_required_required_exception();
    }
///////////////////////////////////////////////////////////////////////
//    以上结论：通过这两个方法我们证明了在外围方法未开启事务的情况下 Propagation.REQUIRED 修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
///////////////////////////////////////////////////////////////////////


    @Test
    public void transaction_exception_required_required() {
//        “张三”、“李四”均未插入。
//        外围方法开启事务，内部方法加入外围方法事务，外围方法回滚，内部方法也要回滚。
        TransactionExample.transaction_exception_required_required();
    }

    @Test
    public void transaction_required_required_exception() {
//        “张三”、“李四”均未插入。
//        外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，外围方法感知异常致使整体事务回滚。
        TransactionExample.transaction_required_required_exception();
    }

    @Test
    public void transaction_required_required_exception_try() {
//        “张三”、“李四”均未插入。
//        外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，即使方法被catch不被外围方法感知，整个事务依然回滚。
        TransactionExample.transaction_required_required_exception_try();
    }
/////////////////////////////////////////////////////////////////////
//    以上结论：以上试验结果证明，在外围方法开启事务的情况下 Propagation.REQUIRED 修饰的内部方法会加入到外围方法的事务中，
//    所有 Propagation.REQUIRED 修饰的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚。
///////////////////////////////////////////////////////////////////////


    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”、“李四”方法也均未开启事务，因为不存在事务所以无论外围方法或者内部方法抛出异常都不会回滚。
     */
    @Test
    public void testNotransaction_supports_supports_exception() {
        TransactionExample.notransaction_supports_supports_exception();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”、“李四”方法也均未开启事务，因为不存在事务所以无论外围方法或者内部方法抛出异常都不会回滚。
     */
    @Test
    public void testNotransaction_exception_supports_supports() {
        TransactionExample.notransaction_exception_supports_supports();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”、“李四”方法都在外围方法的事务中运行，加入外围方法事务，所以三个方法同一个事务。外围方法或内部方法抛出异常，
     * 整个事务全部回滚。
     */
    @Test
    public void testTransaction_supports_supports_exception() {
        TransactionExample.transaction_supports_supports_exception();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”、“李四”方法都在外围方法的事务中运行，加入外围方法事务，所以三个方法同一个事务。外围方法或内部方法抛出异常，
     * 整个事务全部回滚。
     */
    @Test
    public void testTransaction_exception_supports_supports() {
        TransactionExample.transaction_exception_supports_supports();
    }
    // ---------------------------------------------------------------------------------
    // REQUIRED 和 SUPPORTS 在外围方法支持事务的时候没有区别，均加入外围方法的事务中。
    // 当外围方法不支持事务，REQUIRED 开启新的事务而 SUPPORTS 不开启事务。
    // REQUIRED 的事务一定和外围方法事务统一。如果外围方法没有事务，每一个内部 REQUIRED 方法都会开启一个新的事务，互不干扰。
    // ---------------------------------------------------------------------------------

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”、“李四”方法都在自己的事务中独立运行。外围方法抛出异常，插入“张三”、“李四”事务均不回滚。
     */
    @Test
    public void testNotransaction_exception_requiresNew_requiresNew() {
        TransactionExample.notransaction_exception_requiresNew_requiresNew();
    }

    /**
     * 结果：张三（插入），李四（未插入）</br>
     * 外围方法未开启事务，插入“张三”、“李四”方法都在自己的事务中独立运行。插入“李四”方法抛出异常只会导致插入“李四”方法中的事务被回滚，
     * 不会影响插入“张三”方法的事务。
     */
    @Test
    public void testNotransaction_requiresNew_requiresNew_exception() {
        TransactionExample.notransaction_requiresNew_requiresNew_exception();
    }

    /**
     * 结果：张三（未插入），李四（插入），王五（插入）</br>
     * 外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中，
     * 外围方法抛出异常时只回滚 和外围方法同一事务的 方法，故插入“张三”的方法回滚。
     */
    @Test
    public void transaction_exception_required_requiresNew_requiresNew() {
        TransactionExample.transaction_exception_required_requiresNew_requiresNew();
    }

    /**
     * 结果：张三（未插入），李四（插入），王五（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。插入“王五”方法抛出异常，首先插入
     * “王五”方法的事务被回滚，异常继续抛出被外围方法感知，外围方法事务亦被回滚，故插入“张三”方法也被回滚。
     */
    @Test
    public void testTransaction_required_requiresNew_requiresNew_exception() {
        TransactionExample.transaction_required_requiresNew_requiresNew_exception();
    }

    /**
     * 结果：张三（插入），李四（插入），王五（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。插入“王五”方法抛出异常，首先插入
     * “王五”方法的事务被回滚，异常被catch不会被外围方法感知，外围方法事务不回滚，故插入“张三”方法插入成功。
     */
    @Test
    public void transaction_required_requiresNew_requiresNew_exception_try() {
        TransactionExample.transaction_required_requiresNew_requiresNew_exception_try();
    }
    // ---------------------------------------------------------------------------------
    // REQUIRES_NEW 标注方法无论外围方法是否开启事务，内部 REQUIRES_NEW 方法均会开启独立事务，且和外围方法也不在同一个事务中，内部方法和外围方法、内部方法之间均不相互干扰。
    // 当外围方法不开启事务的时候，REQUIRED 和 REQUIRES_NEW 标注的内部方法效果相同。
    // ---------------------------------------------------------------------------------

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”方法在自己的事务中运行，插入“李四”方法不在任何事务中运行。外围方法抛出异常，但是外围方法没有事务，
     * 所以其他内部事务方法不会被回滚，非事务方法更不会被回滚。
     */
    @Test
    public void testNotransaction_exception_required_notSuppored() {
        TransactionExample.notransaction_exception_required_notSuppored();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”方法在自己的事务中运行，插入“李四”方法不在任何事务中运行。
     * 插入“李四”方法抛出异常，首先因为插入“李四”方法没有开启事务，所以“李四”方法不会回滚，外围方法感知异常，但是因为外围方法没有事务，
     * 所有外围方法也不会回滚。
     */
    @Test
    public void testNotransaction_required_notSuppored_exception() {
        TransactionExample.notransaction_required_notSuppored_exception();
    }

    /**
     * 结果：张三（未插入），李四（插入）</br>
     * 外围方法开启事务，因为插入“张三”方法传播为required，所以和外围方法同一个事务。插入“李四”方法不在任何事务中运行。
     * 外围方法抛出异常，外围方法所在的事务将会回滚，因为插入“张三”方法和外围方法同一个事务，所以将会回滚。
     */
    @Test
    public void testTransaction_exception_required_notSuppored() {
        TransactionExample.transaction_exception_required_notSuppored();
    }

    /**
     * 结果：张三（未插入），李四（插入）</br>
     * 外围方法开启事务，因为插入“张三”方法传播为required，所以和外围方法同一个事务。插入“李四”方法不在任何事务中运行。
     * 插入“李四”方法抛出异常，因为此方法不开启事务，所以此方法不会被回滚，外围方法接收到了异常，所以外围事务需要回滚，因插入“张三”
     * 方法和外围方法同一事务，故被回滚。
     */
    @Test
    public void testTransaction_required_notSuppored_exception() {
        TransactionExample.transaction_required_notSuppored_exception();
    }

    // ---------------------------------------------------------------------------------
    // NOT_SUPPORTED 明确表示不开启事务。
    // ---------------------------------------------------------------------------------

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 结果：张三（未插入）</br>
     * 外围方法未开启事务。内部插入“张三”方法执行的时候因外围没有事务而直接抛出异常，具体插入方法都没有机会执行。
     */
    @Test
    public void testNotransaction_mandatory() {
        TransactionExample.notransaction_mandatory();
    }


    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法都加入外围方法事务，外围方法抛出异常，事务回滚。
     */
    @Test
    public void testTransaction_exception_mandatory_mandatory() {
        TransactionExample.transaction_exception_mandatory_mandatory();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法都加入外围方法事务，内部方法抛出异常，整个事务回滚。
     */
    @Test
    public void testTransaction_mandatory_mandatory_exception() {
        TransactionExample.transaction_mandatory_mandatory_exception();
    }

    // ----------------------------------------------------------------
    // PROPAGATION_MANDATORY	使用当前的事务，如果当前没有事务，就抛出异常。
    // ----------------------------------------------------------------

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 结果：张三（未插入</br>
     * 外围方法开启事务。内部插入“张三”方法执行的时候因外围有事务而直接抛出异常，具体插入方法都没有机会执行。
     */
    @Test
    public void testTransaction_never() {
        TransactionExample.transaction_never();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”方法和插入“李四”方法也均无事务，任何异常都不会回滚。
     */
    @Test
    public void testNotransaction_exception_never_never() {
        TransactionExample.notransaction_exception_never_never();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”方法和插入“李四”方法也均无事务，任何异常都不会回滚。
     */
    @Test
    public void testNotransaction_never_never_exception() {
        TransactionExample.notransaction_never_never_exception();
    }

    // --------------------------------------------------------------------------------
    // PROPAGATION_NEVER	以非事务方式执行，如果当前存在事务，则抛出异常。
    // --------------------------------------------------------------------------------


    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法为外围方法的子事务，外围方法事务回滚，相应的子事务也会回滚。
     */
    @Test
    public void transaction_exception_nested_nested() {
        TransactionExample.transaction_exception_nested_nested();
    }

    /**
     * 结果：张三（未插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法为外围方法的子事务，插入“李四”方法抛出异常，相应的子事务回滚，
     * 异常被外围方法感知，外围方法事务回滚，其他子事务即插入“张三”方法事务也回滚了。
     */
    @Test
    public void testTransaction_nested_nested_exception() {
        TransactionExample.transaction_nested_nested_exception();
    }

    /**
     * 结果：张三（插入），李四（插入）</br>
     * 外围方法未开启事务，插入“张三”方法和插入“李四”方法分别开启自己的事务，外围方法事务回滚，所有方法均不回滚。
     */
    @Test
    public void testNotransaction_exception_nested_nested() {
        TransactionExample.notransaction_exception_nested_nested();
    }

    /**
     * 结果：张三（插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法分别开启自己的事务，插入“李四”方法抛出异常，相应的子事务回滚，
     * 异常被外围方法感知，外围方法无事务所以无需回滚，故插入“张三”方法没有回滚。
     */
    @Test
    public void testnotransaction_nested_nested_exception() {
        TransactionExample.notransaction_nested_nested_exception();
    }

    //------------------------------------------
    // 在外围方法不开启事务的时候，NESTED 和 REQUIRED 行为类似，均开新开事务。
    //------------------------------------------

    /**
     * 结果：张三（插入），李四（未插入）</br>
     * 外围方法开启事务，插入“张三”方法和插入“李四”方法为外围方法的子事务，插入“李四”方法抛出异常，相应的子事务回滚，异常被捕获外围方法不可知，故外围方法事务无需回滚。
     */
    @Test
    public void transaction_nested_nested_exception_try() {
        TransactionExample.transaction_nested_nested_exception_try();
    }

    //------------------------------------------------------------------------------
    /**
     * NESTED 和 REQUIRED 修饰的内部方法都属于外围方法事务，如果外围方法抛出异常，这两种方法的事务都会被回滚。
     *
     * 但是 REQUIRED 是加入外围方法事务，所以和外围事务同属于一个事务，所以一旦 REQUIRED 事务抛出异常被回滚，外围方法事务也将被回滚。{@link #transaction_required_required_exception_try}
     * 而 NESTED 是外围方法的子事务，有单独的保存点，所以 NESTED 方法抛出异常被回滚，不会影响到外围方法的事务。{@link #transaction_nested_nested_exception_try}
     *
     * NESTED 和 REQUIRES_NEW 都可以做到内部方法事务回滚但是不影响外围方法事务。{@link #transaction_required_requiresNew_requiresNew_exception_try}
     * 但是因为 NESTED 是嵌套事务，所以外围方法回滚之后，作为外围方法事务的子事务也会被回滚。{@link #transaction_exception_nested_nested}
     *
     * 而 REQUIRES_NEW 是通过开启新的事务实现的，内部事务和外围事务是两个事务，外围事务回滚不会影响内部事务。{@link #transaction_exception_required_requiresNew_requiresNew}
     */
    //------------------------------------------------------------------------------


    //------------------------------------------------------------------------------
    //事务之间可见性试验，进一步说明新开事务和嵌套事务之间的区别。
    //------------------------------------------------------------------------------

    /**
     * 结果：getRequired 可见，get 可见</br>
     * 外围方法未开启事务，addRequired 在自己的事务中运行，执行外之后即对外可见，故 getRequired 和 get 都可见事务执行结果。
     */
    @Test
    public void testNotransaction_addRequired_getRequired_get() {
        TransactionExample.notransaction_addRequired_getRequired_get();
    }

    /**
     * 结果：getRequired 可见，get 可见</br>
     * 外围方法开启事务，addRequired 和外围方法同事务，getRequired 和 get 都和外围方法同一个事务，故均可见 addRequired 执行之后的结果。
     */
    @Test
    public void testTransaction_addRequired_getRequired_get() {
        TransactionExample.transaction_addRequired_getRequired_get();
    }

    /**
     * 结果：getNested 可见，get 可见</br>
     * 外围方法开启事务，addRequired 和外围方法同事务，getNested 属于外围事务子事务，get 属于外围事务，故均可见 addRequired 执行之后的结果。
     */
    @Test
    public void testTransaction_addRequired_getNested_get() {
        TransactionExample.transaction_addRequired_getNested_get();
    }

    /**
     * 结果：get 可见</br>
     * 外围方法开启事务，addRequired 和外围方法同事务，getNotSuppored 不支持事务，并将外围事务挂起，
     * getNotSuppored 不在 addRequired 事务范围中，由于事务隔离性，getNotSuppored 看不到 addRequired 的执行结果。
     * ，get 属于外围事务，故可见 addRequired 执行之后的结果。
     */
    @Test
    public void testTransaction_addRequired_getNotSuppored_get() {
        TransactionExample.transaction_addRequired_getNotSuppored_get();
    }

    /**
     * 结果：get可见</br>
     * 外围方法开启事务，addRequired 和外围方法同事务，getRequiresNew 新开事务，并将外围事务挂起，
     * 由于事务隔离性，getRequiresNew 看不到 addRequired 的执行结果。
     * get 属于外围事务，故可见 addRequired 执行之后的结果。
     */
    @Test
    public void testTransaction_addRequired_getRequiresNew_get() {
        TransactionExample.transaction_addRequired_getRequiresNew_get();
    }

    //------------------------------------------------------------------------------
    // 以上结果说明:
    // 对于 REQUIRES_NEW 和 NOT_SUPPORTED 传播属性，在外围方法开启事务，均不可见外围事务执行结果。
    // 对于 REQUIRED 和 NESTED 传播属性，无论外围方法是否开启事务，均可见外围方法执行结果。
    //------------------------------------------------------------------------------


}
