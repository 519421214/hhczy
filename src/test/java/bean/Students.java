package bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Getter 和 @Setter:添加 @Getter 和 @Setter 注解用在 Java Bean 类上面，无需生成 get/ set 方法，会自动生成所有的 get/ set 方法及一个默认的构造方法。
 *                      注解也可以单独使用在字段上面，同样也会生成对应的 get/ set 方法及一个默认的构造方法。
 * @ToString
 * @NoArgsConstructor: 用在类上，用来生成一个默认的无参构造方法。
 * @RequiredArgsConstructor: 用在类上，使用类中所有带有 @NonNull 注解和 final 类型的字段生成对应的构造方法。
 * @AllArgsConstructor: 用在类上，生成一个所有参数的构造方法，默认不提供无参构造方法。
 * @Data: 用在类上，等同于下面这几个注解合集 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
 * @Value: 用在类上，等同于下面这几个注解合集。 @Getter @FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE) @AllArgsConstructor @ToString @EqualsAndHashCode}
 * @NonNull： 用在属性上，用于字段的非空检查，如果传入到 set 方法中的值为空，则抛出空指针异常，该注解也会生成一个默认的构造方法。
 */
@Data
@AllArgsConstructor
public class Students {
//    @Getter
    private Integer id;
    private String name;
    private Integer age;












}
