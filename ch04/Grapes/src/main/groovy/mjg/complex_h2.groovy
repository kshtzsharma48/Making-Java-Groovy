package mjg

@GrabConfig(systemClassLoader=true)
@Grapes([
    @Grab('org.apache.commons:commons-math3:3.0'),
    @Grab(group='com.h2database', module='h2', version='1.2.140')
])

import static java.lang.Math.*
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.complex.ComplexUtils
import groovy.sql.Sql

Sql sql = Sql.newInstance(url:'jdbc:h2:mem:',driver:'org.h2.Driver')

sql.execute '''
    create table coordinates (
        id bigint generated by default as identity,
        angle double not null,
        x double not null,
        y double not null,
        primary key (id)
    )
'''

int n = 20
def delta = 2*PI/n
(0..<n).each { i ->
    Complex c = ComplexUtils.polar2Complex(1, i*delta)
    sql.execute """
    insert into coordinates(id,angle,x,y) values(null, ${i*delta}, $c.real, $c.imaginary)
    """
}

sql.rows('select * from coordinates').each { row ->
    println "$row.id, $row.angle, $row.x, $row.y"
}
